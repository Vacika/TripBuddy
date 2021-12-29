package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.ReservationRequest
import com.project.najdiprevoz.enums.NotificationType
import com.project.najdiprevoz.enums.AllowedActions
import com.project.najdiprevoz.enums.ReservationStatus
import com.project.najdiprevoz.enums.TripStatus
import com.project.najdiprevoz.events.TripCancelledEvent
import com.project.najdiprevoz.exceptions.*
import com.project.najdiprevoz.repositories.ReservationRequestRepository
import com.project.najdiprevoz.web.request.create.CreateReservationRequestForTrip
import javassist.NotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import javax.transaction.Transactional

@Service
class ReservationRequestService(
    private val repository: ReservationRequestRepository,
    private val tripService: TripService,
    private val userService: UserService,
    private val notificationService: NotificationService
) {

    val logger: Logger = LoggerFactory.getLogger(ReservationRequestService::class.java)


    fun findById(id: Long): ReservationRequest =
        repository.findById(id)
            .orElseThrow { TripNotFoundException() }

    @Transactional
    fun create(req: CreateReservationRequestForTrip, username: String) = with(req) {
        validateRequest(this, username)
        val savedRequest = repository.save(
            ReservationRequest(
                status = ReservationStatus.PENDING,
                trip = tripService.findById(tripId),
                createdOn = ZonedDateTime.now(),
                requester = userService.findByUsername(username),
                additionalDescription = additionalDescription,
                requestedSeats = requestedSeats
            )
        )
        logger.debug("[ReservationRequestService] Successfully added new ReservationRequest")
        pushNotification(savedRequest, NotificationType.REQUEST_SENT)
    }

    @Transactional
    @Modifying
    fun changeStatus(id: Long, newStatus: ReservationStatus) =
        updateStatusIfPossible(requestId = id, previousStatus = findById(id).status, newStatus = newStatus)

    fun getAvailableActions(requestId: Long, forRequester: Boolean): List<String> {
        val reservationRequest = findById(requestId)
        val currentStatus = reservationRequest.status
        var availableActions = listOf<String>()
        if (!forRequester && reservationRequest.trip.status == TripStatus.ACTIVE) {
            if (changeStatusActionAllowed(currentStatus, ReservationStatus.APPROVED))
                availableActions = availableActions.plus(AllowedActions.APPROVE_RESERVATION.name)
            if (changeStatusActionAllowed(currentStatus, ReservationStatus.DENIED))
                availableActions = availableActions.plus(AllowedActions.DENY_RESERVATION.name)
        } else if (forRequester) {
            if (canSubmitRating(reservationRequest))
                availableActions = availableActions.plus(AllowedActions.SUBMIT_RATING.name)
            if (changeStatusActionAllowed(
                    currentStatus,
                    ReservationStatus.CANCELLED
                ) && reservationRequest.trip.status == TripStatus.ACTIVE
            )
                availableActions = availableActions.plus(AllowedActions.CANCEL_RESERVATION.name)
        }
        return availableActions
    }

    /** EVENT LISTENER **/
    @EventListener
    fun onTripCancelled(event: TripCancelledEvent) {
        event.trip.reservationRequests.filter {
            it.status == ReservationStatus.PENDING
                    || it.status == ReservationStatus.APPROVED
        }
            .forEach { resRequest ->
                changeRequestToTripCancelled(resRequest.id)
            }
    }

    /** Checks before creating / updating reservation **/
    //TODO: Switch with translatable Exception messages, switch with custom exceptions
    private fun validateRequest(req: CreateReservationRequestForTrip, username: String) = with(req) {
        logger.debug("[ReservationRequestValidator] Validating new ReservationRequest..")
        if (checkIfAppliedBefore(tripId, username)) {
            logger.error("[ReservationRequestValidator] User [$username] has already sent a reservation request for Trip [$tripId]")
            throw AlreadySentReservationException()
        }
        if (!checkIsTripActive(tripId)) {
            logger.error("[ReservationRequestValidator] Trip applied for seat is not ACTIVE! Trip ID: [$tripId]")
            throw TripNotActiveException()
        }
        if (!checkIfEnoughAvailableSeats(tripId, requestedSeats)) {
            logger.error("[ReservationRequestValidator] Trip applied for seat does not have $requestedSeats seats available! Trip ID: [$tripId]")
            throw NotEnoughAvailableSeatsException()
        }
        if (!checkIfNotDriverItself(username, tripId)) {
            logger.error("[ReservationRequestValidator] You can't create a reservation request for a trip published by you! Username: [$username], TripID: [$tripId]")
            throw OwnTripReservationApplyException()
        }
        logger.debug("[ReservationRequestValidator] New ReservationRequest is successfully validated..")

    }

    private fun checkIfNotDriverItself(username: String, tripId: Long): Boolean =
        this.tripService.findById(tripId).driver.username != username

    private fun checkIfEnoughAvailableSeats(tripId: Long, requestedSeats: Int): Boolean =
        this.tripService.findById(tripId).availableSeats >= requestedSeats

    private fun checkIfAppliedBefore(tripId: Long, username: String): Boolean {
        val reservationRequest = repository.findByTripIdAndRequesterUsername(tripId, username)
        if (reservationRequest.isEmpty()) {
            return false
        }
        return true
    }

    private fun checkIsTripActive(tripId: Long): Boolean {
        return this.tripService.findById(tripId).status == TripStatus.ACTIVE
    }

    private fun canSubmitRating(reservationRequest: ReservationRequest): Boolean =
        reservationRequest.rating != null && reservationRequest.trip.status == TripStatus.FINISHED
                && reservationRequest.status == ReservationStatus.APPROVED


    /** STATUS CHANGES **/
    private fun changeStatusActionAllowed(previousStatus: ReservationStatus, nextStatus: ReservationStatus): Boolean {
        if (previousStatus != nextStatus) {
            return when (previousStatus) {
                ReservationStatus.APPROVED -> nextStatus == ReservationStatus.CANCELLED || nextStatus == ReservationStatus.FINISHED
                ReservationStatus.PENDING -> nextStatus != ReservationStatus.PENDING
                ReservationStatus.CANCELLED -> false
                ReservationStatus.DENIED -> false
                ReservationStatus.TRIP_CANCELLED -> false
                ReservationStatus.EXPIRED -> false
                ReservationStatus.FINISHED -> false
            }
        }
        return false
    }

    private fun updateStatusIfPossible(
        requestId: Long, previousStatus: ReservationStatus,
        newStatus: ReservationStatus
    ) {
        logger.debug("[ReservationRequestService] Checking if reservation request status transition is valid..")
        if (changeStatusActionAllowed(previousStatus, newStatus)) {
            logger.debug("[ReservationRequestService] Reservation request status transition from $previousStatus to $newStatus is VALID, changing status..")
            return when (newStatus) {
                ReservationStatus.APPROVED -> approveRequest(requestId)
                ReservationStatus.DENIED -> denyRequest(requestId)
                ReservationStatus.PENDING -> changeRequestToPending(requestId)
                ReservationStatus.CANCELLED -> cancelRequest(requestId)
                ReservationStatus.TRIP_CANCELLED -> changeRequestToTripCancelled(requestId)
                ReservationStatus.EXPIRED -> expireRequest(requestId)
                ReservationStatus.FINISHED -> finishRequest(requestId);
            }
        } else throw RuntimeException(
            "Status change not allowed from $previousStatus to $newStatus for ReservationRequest ID: [$requestId]"
        )
    }

    private fun approveRequest(requestId: Long) {
        val reservationRequest = findById(requestId)
        if (reservationRequest.trip.availableSeats >= reservationRequest.requestedSeats) {
            repository.updateReservationRequestStatus(requestId = requestId, status = ReservationStatus.APPROVED)
            tripService.updateAvailableSeats(
                tripId = reservationRequest.trip.id,
                seats = reservationRequest.trip.availableSeats - reservationRequest.requestedSeats
            )
        } else throw RuntimeException("EXCEPTION_NOT_ENOUGH_SEATS_AVAILABLE_TO_APPROVE_REQUEST")
        pushNotification(reservationRequest, NotificationType.REQUEST_APPROVED)
    }

    private fun denyRequest(requestId: Long) {
        repository.updateReservationRequestStatus(requestId = requestId, status = ReservationStatus.DENIED)
        pushNotification(findById(requestId), NotificationType.REQUEST_DENIED)
    }

    private fun cancelRequest(requestId: Long) {
        val reservationRequest = findById(requestId)
        tripService.updateAvailableSeats(
            tripId = reservationRequest.trip.id,
            seats = reservationRequest.trip.availableSeats + reservationRequest.requestedSeats
        )
        repository.updateReservationRequestStatus(requestId = requestId, status = ReservationStatus.CANCELLED)
        pushNotification(findById(requestId), NotificationType.REQUEST_CANCELLED)
    }

    private fun changeRequestToTripCancelled(requestId: Long) {
        repository.updateReservationRequestStatus(requestId = requestId, status = ReservationStatus.TRIP_CANCELLED)
        pushNotification(findById(requestId), NotificationType.TRIP_CANCELLED)
    }

    private fun expireRequest(requestId: Long) {
        repository.updateReservationRequestStatus(requestId = requestId, status = ReservationStatus.EXPIRED)
        pushNotification(findById(requestId), NotificationType.REQUEST_EXPIRED)
    }

    private fun finishRequest(requestId: Long) {
        repository.updateReservationRequestStatus(requestId = requestId, status = ReservationStatus.FINISHED)
    }

    private fun changeRequestToPending(requestId: Long) {
        repository.updateReservationRequestStatus(requestId = requestId, status = ReservationStatus.PENDING)
        pushNotification(findById(requestId), NotificationType.REQUEST_SENT)
    }

    private fun pushNotification(reservationRequest: ReservationRequest, notificationType: NotificationType) {
        logger.debug("[ReservationRequestService] Pushing ReservationRequest Status Notification..")
        notificationService.pushReservationStatusChangeNotification(
            reservationRequest = reservationRequest,
            notificationType = notificationType
        )
    }

    /** CRON JOB -- EXPIRING OLD RESERVATION REQUEST **/
    fun reservationRequestCronJob(reservationRequest: ReservationRequest) {
        logger.debug("[ReservationRequestService] Updating ReservationRequest ${reservationRequest.id} to EXPIRED..")
        val request = findById(reservationRequest.id)
        request.status = ReservationStatus.EXPIRED
        repository.save(request)
        pushNotification(request, NotificationType.REQUEST_EXPIRED)
    }
}
