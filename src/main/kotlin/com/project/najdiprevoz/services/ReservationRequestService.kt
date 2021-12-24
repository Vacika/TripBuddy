package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.ReservationRequest
import com.project.najdiprevoz.enums.NotificationType
import com.project.najdiprevoz.enums.Actions
import com.project.najdiprevoz.enums.ReservationStatus
import com.project.najdiprevoz.enums.TripStatus
import com.project.najdiprevoz.events.TripCancelledEvent
import com.project.najdiprevoz.exceptions.AlreadySentReservationException
import com.project.najdiprevoz.exceptions.NotEnoughAvailableSeatsException
import com.project.najdiprevoz.exceptions.OwnTripReservationApplyException
import com.project.najdiprevoz.exceptions.TripNotActiveException
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
            .orElseThrow { NotFoundException("Ride request not found!") }

    @Transactional
    @Modifying
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
                availableActions = availableActions.plus(Actions.APPROVE.name)
            if (changeStatusActionAllowed(currentStatus, ReservationStatus.DENIED))
                availableActions = availableActions.plus(Actions.DENY.name)
        } else if (forRequester) {
            if (canSubmitRating(reservationRequest))
                availableActions = availableActions.plus(Actions.SUBMIT_RATING.name)
            if (changeStatusActionAllowed(currentStatus, ReservationStatus.CANCELLED))
                availableActions = availableActions.plus(Actions.CANCEL_RESERVATION.name)
        }
        return availableActions
    }

    /** EVENT LISTENER **/
    @EventListener
    fun onTripCancelled(event: TripCancelledEvent) {
        event.trip.reservationRequests
            .forEach { resRequest ->
                changeStatus(resRequest.id, ReservationStatus.RIDE_CANCELLED)
                notificationService.pushReservationStatusChangeNotification(
                    resRequest,
                    NotificationType.RIDE_CANCELLED
                )
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
                ReservationStatus.APPROVED -> nextStatus == ReservationStatus.CANCELLED
                ReservationStatus.PENDING -> nextStatus != ReservationStatus.PENDING
                ReservationStatus.CANCELLED -> false
                ReservationStatus.DENIED -> false
                ReservationStatus.RIDE_CANCELLED -> false
                ReservationStatus.EXPIRED -> false
            }
        }
        return false
    }

    private fun updateStatusIfPossible(
        requestId: Long, previousStatus: ReservationStatus,
        newStatus: ReservationStatus
    ) {
        logger.debug("[ReservationRequestService] Checking if ride request status transition is valid..")
        val request = repository.findById(requestId)
        if (changeStatusActionAllowed(previousStatus, newStatus) && request.get().trip.status == TripStatus.ACTIVE) {
            logger.debug("[ReservationRequestService]Ride request status transition from $previousStatus to $newStatus is VALID, changing status..")
            return when (newStatus) {
                ReservationStatus.APPROVED -> approveRequest(requestId)
                ReservationStatus.DENIED -> denyRequest(requestId)
                ReservationStatus.PENDING -> changeRequestToPending(requestId)
                ReservationStatus.CANCELLED -> cancelRequest(requestId)
                ReservationStatus.RIDE_CANCELLED -> changeRequestToRideCancelled(requestId)
                ReservationStatus.EXPIRED -> expireRequest(requestId)
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
        } else throw RuntimeException("Not enough seats available to approve ReservationRequest with ID: [$requestId]!")
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

    private fun changeRequestToRideCancelled(requestId: Long) {
        repository.updateReservationRequestStatus(requestId = requestId, status = ReservationStatus.RIDE_CANCELLED)
        pushNotification(findById(requestId), NotificationType.RIDE_CANCELLED)
    }

    private fun expireRequest(requestId: Long) {
        repository.updateReservationRequestStatus(requestId = requestId, status = ReservationStatus.EXPIRED)
        pushNotification(findById(requestId), NotificationType.REQUEST_EXPIRED)
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
