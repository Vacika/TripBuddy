package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.ReservationRequest
import com.project.najdiprevoz.enums.NotificationType
import com.project.najdiprevoz.enums.Actions
import com.project.najdiprevoz.enums.ReservationStatus
import com.project.najdiprevoz.enums.TripStatus
import com.project.najdiprevoz.repositories.ReservationRequestRepository
import com.project.najdiprevoz.web.request.create.CreateRequestForTrip
import javassist.NotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import javax.transaction.Transactional

@Service
class ReservationRequestService(private val repository: ReservationRequestRepository,
                         private val tripService: TripService,
                         private val userService: UserService,
                         private val notificationService: NotificationService) {

    val logger: Logger = LoggerFactory.getLogger(ReservationRequestService::class.java)

    fun findAll(): List<ReservationRequest> =
            repository.findAll()

    fun findById(id: Long): ReservationRequest =
            repository.findById(id)
                    .orElseThrow { NotFoundException("Ride request not found!") }

    fun getAllRequestsByTripId(rideId: Long): List<ReservationRequest> =
            repository.findAllByTripId(rideId)

    //Gets request sent from the user
    fun getSentReservationRequests(username: String) =
            repository.findAllByRequesterUsername(username = username)
                    .sortedBy { it.status.ordinal }

    //Gets received requests for user
    fun getReceivedReservationRequests(username: String) =
            repository.findReceivedRequests(username = username)
                    .sortedBy { it.status.ordinal }

    fun getRequestsForRideByStatus(rideId: Long, status: ReservationStatus): List<ReservationRequest> =
            repository.findAllByStatusAndTripId(status, rideId)
                    .sortedBy { it.status.ordinal }

    @Modifying
    @Transactional
    fun changeStatus(id: Long, newStatus: ReservationStatus) =
            updateStatusIfPossible(requestId = id, previousStatus = findById(id).status, newStatus = newStatus)

    @Modifying
    @Transactional
    fun addNewReservationRequest(req: CreateRequestForTrip, username: String) = with(req) {
        validateRequest(this, username)
        val savedRequest = repository.save(ReservationRequest(
                status = ReservationStatus.PENDING,
                trip = tripService.findById(tripId),
                createdOn = ZonedDateTime.now(),
                requester = userService.findUserByUsername(username),
                additionalDescription = additionalDescription,
                requestedSeats = requestedSeats))
        logger.debug("[ReservationRequestService] Sucessfully added new ReservationRequest")
        pushNotification(savedRequest, NotificationType.REQUEST_SENT)
    }

    private fun validateRequest(req: CreateRequestForTrip, username: String) = with(req) {
        logger.debug("[ReservationRequestService] Validating new ReservationRequest..")
        if (checkIfAppliedBefore(tripId, username)) {
            throw RuntimeException("User [$username] has already sent a ride request for Trip [$tripId]")
        }
        if (!checkIsTripActive(tripId)) {
            throw RuntimeException("Trip applied for seat is not ACTIVE! Trip ID: [$tripId]")
        }
        if (!checkIfEnoughAvailableSeats(tripId, requestedSeats)) {
            throw RuntimeException(
                    "Trip applied for seat does not have $requestedSeats seats available! Trip ID: [$tripId]")
        }
        if (!checkIfNotDriverItself(username, tripId)) {
            throw RuntimeException(
                    "You can't create a ride request for a ride published by you! Username: [$username], TripID: [$tripId]")
        }
        logger.debug("[ReservationRequestService] New ReservationRequest is successfully validated..")

    }

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

    private fun checkIfNotDriverItself(username: String, tripId: Long): Boolean =
            this.tripService.findById(tripId).driver.username != username

    private fun checkIfEnoughAvailableSeats(tripId: Long, requestedSeats: Int): Boolean =
            this.tripService.findById(tripId).availableSeats >= requestedSeats

    private fun checkIfAppliedBefore(tripId: Long, username: String): Boolean {
        val reservationRequest = repository.findByTripIdAndRequesterUsername(tripId, username)
        if (reservationRequest.isNullOrEmpty()) {
            return false
        }
        return true
    }

    private fun changeRequestToApproved(requestId: Long) {
        val reservationRequest = findById(requestId)
        if (reservationRequest.trip.availableSeats >=  reservationRequest.requestedSeats) {
            repository.updateReservationRequestStatus(requestId = requestId, status = ReservationStatus.APPROVED)
            tripService.updateTripAvailableSeats(rideId = reservationRequest.trip.id, seats = reservationRequest.trip.availableSeats - reservationRequest.requestedSeats)
        } else throw RuntimeException("Not enough seats available to approve ReservationRequest with ID: [$requestId]!")
        pushNotification(reservationRequest, NotificationType.REQUEST_APPROVED)
    }

    private fun changeRequestToDenied(requestId: Long) {
        repository.updateReservationRequestStatus(requestId = requestId, status = ReservationStatus.DENIED)
        pushNotification(findById(requestId), NotificationType.REQUEST_DENIED)
    }

    private fun changeRequestToCancelled(requestId: Long) {
        val reservationRequest = findById(requestId)
        tripService.updateTripAvailableSeats(rideId = reservationRequest.trip.id, seats = reservationRequest.trip.availableSeats + reservationRequest.requestedSeats)
        repository.updateReservationRequestStatus(requestId = requestId, status = ReservationStatus.CANCELLED)
        pushNotification(findById(requestId), NotificationType.REQUEST_CANCELLED)
    }

    private fun changeRequestToRideCancelled(requestId: Long) {
        repository.updateReservationRequestStatus(requestId = requestId, status = ReservationStatus.RIDE_CANCELLED)
        pushNotification(findById(requestId), NotificationType.RIDE_CANCELLED)
    }

    private fun changeRequestToExpired(requestId: Long) {
        repository.updateReservationRequestStatus(requestId = requestId, status = ReservationStatus.EXPIRED)
        pushNotification(findById(requestId), NotificationType.REQUEST_EXPIRED)
    }

    private fun changeRequestToPending(requestId: Long) {
        repository.updateReservationRequestStatus(requestId = requestId, status = ReservationStatus.PENDING)
        pushNotification(findById(requestId), NotificationType.REQUEST_SENT)
    }

    private fun updateStatusIfPossible(requestId: Long, previousStatus: ReservationStatus,
                                       newStatus: ReservationStatus) {
        logger.debug("[ReservationRequestService] Checking if ride request status transition is valid..")
        val request = repository.findById(requestId)
        if (changeStatusActionAllowed(previousStatus, newStatus) && request.get().trip.status == TripStatus.ACTIVE) {
            logger.debug("[ReservationRequestService]Ride request status transition from $previousStatus to $newStatus is VALID, changing status..")
            when (newStatus) {
                ReservationStatus.APPROVED -> changeRequestToApproved(requestId)
                ReservationStatus.DENIED -> changeRequestToDenied(requestId)
                ReservationStatus.PENDING -> changeRequestToPending(requestId)
                ReservationStatus.CANCELLED -> changeRequestToCancelled(requestId)
                ReservationStatus.RIDE_CANCELLED -> changeRequestToRideCancelled(requestId)
                ReservationStatus.EXPIRED -> changeRequestToExpired(requestId)
            }
        } else throw RuntimeException(
                "Status change not allowed from $previousStatus to $newStatus for ReservationRequest ID: [$requestId]")
    }

    private fun canSubmitRating(reservationRequest: ReservationRequest): Boolean =
            reservationRequest.rating != null && reservationRequest.trip.status == TripStatus.FINISHED
                    && reservationRequest.status == ReservationStatus.APPROVED

    private fun checkIsTripActive(tripId: Long): Boolean {
        return this.tripService.findById(tripId).status == TripStatus.ACTIVE
    }

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

    private fun pushNotification(reservationRequest: ReservationRequest, notificationType: NotificationType) {
        logger.debug("[ReservationRequestService] Pushing ReservationRequest Status Notification..")
        notificationService.pushReservationRequestStatusChangeNotification(reservationRequest = reservationRequest, notificationType = notificationType)
    }

    fun reservationRequestCronJob(reservationRequest: ReservationRequest) {
        logger.debug("[ReservationRequestService] Updating ReservationRequest ${reservationRequest.id} to EXPIRED..")
        val request = findById(reservationRequest.id)
        request.status = ReservationStatus.EXPIRED
        repository.save(request)
        pushNotification(request, NotificationType.REQUEST_EXPIRED)
    }
}
