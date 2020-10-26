package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.RideRequest
import com.project.najdiprevoz.enums.NotificationType
import com.project.najdiprevoz.enums.Actions
import com.project.najdiprevoz.enums.RideRequestStatus
import com.project.najdiprevoz.enums.RideStatus
import com.project.najdiprevoz.repositories.RideRequestRepository
import com.project.najdiprevoz.web.request.create.CreateRequestForTrip
import javassist.NotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import javax.transaction.Transactional

@Service
class RideRequestService(private val repository: RideRequestRepository,
                         private val tripService: TripService,
                         private val userService: UserService,
                         private val notificationService: NotificationService) {

    val logger: Logger = LoggerFactory.getLogger(RideRequestService::class.java)

    fun findAll(): List<RideRequest> =
            repository.findAll()

    fun findById(id: Long): RideRequest =
            repository.findById(id)
                    .orElseThrow { NotFoundException("Ride request not found!") }

    fun getAllRequestsByTripId(rideId: Long): List<RideRequest> =
            repository.findAllByRideId(rideId)

    //Gets request sent from the user
    fun getSentRideRequests(username: String) =
            repository.findAllByRequesterUsername(username = username)
                    .sortedBy { it.status.ordinal }

    //Gets received requests for user
    fun getReceivedRideRequests(username: String) =
            repository.findReceivedRequests(username = username)
                    .sortedBy { it.status.ordinal }

    fun getRequestsForRideByStatus(rideId: Long, status: RideRequestStatus): List<RideRequest> =
            repository.findAllByStatusAndRideId(status, rideId)
                    .sortedBy { it.status.ordinal }

    @Modifying
    @Transactional
    fun changeStatus(id: Long, newStatus: RideRequestStatus) =
            updateStatusIfPossible(requestId = id, previousStatus = findById(id).status, newStatus = newStatus)

    @Modifying
    @Transactional
    fun addNewRideRequest(req: CreateRequestForTrip, username: String) = with(req) {
        validateRequest(this, username)
        val savedRequest = repository.save(RideRequest(
                status = RideRequestStatus.PENDING,
                ride = tripService.findById(tripId),
                createdOn = ZonedDateTime.now(),
                requester = userService.findUserByUsername(username),
                additionalDescription = additionalDescription,
                requestedSeats = requestedSeats))
        logger.debug("[RideRequestService] Sucessfully added new RideRequest")
        pushNotification(savedRequest, NotificationType.REQUEST_SENT)
    }

    private fun validateRequest(req: CreateRequestForTrip, username: String) = with(req) {
        logger.debug("[RideRequestService] Validating new RideRequest..")
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
        logger.debug("[RideRequestService] New RideRequest is successfully validated..")

    }

    fun getAvailableActions(requestId: Long, forRequester: Boolean): List<String> {
        val rideRequest = findById(requestId)
        val currentStatus = rideRequest.status
        var availableActions = listOf<String>()
        if (!forRequester && rideRequest.ride.status == RideStatus.ACTIVE) {
            if (changeStatusActionAllowed(currentStatus, RideRequestStatus.APPROVED))
                availableActions = availableActions.plus(Actions.APPROVE.name)
            if (changeStatusActionAllowed(currentStatus, RideRequestStatus.DENIED))
                availableActions = availableActions.plus(Actions.DENY.name)
        } else if (forRequester) {
            if (canSubmitRating(rideRequest))
                availableActions = availableActions.plus(Actions.SUBMIT_RATING.name)
            if (changeStatusActionAllowed(currentStatus, RideRequestStatus.CANCELLED))
                availableActions = availableActions.plus(Actions.CANCEL_RESERVATION.name)
        }
        return availableActions
    }

    private fun checkIfNotDriverItself(username: String, tripId: Long): Boolean =
            this.tripService.findById(tripId).driver.username != username

    private fun checkIfEnoughAvailableSeats(tripId: Long, requestedSeats: Int): Boolean =
            this.tripService.findById(tripId).availableSeats >= requestedSeats

    private fun checkIfAppliedBefore(tripId: Long, username: String): Boolean {
        val rideRequest = repository.findByRideIdAndRequester_Username(tripId, username)
        if (rideRequest.isPresent && rideRequest.get().status != RideRequestStatus.CANCELLED) {
            return true
        }
        return false
    }

    private fun changeRequestToApproved(requestId: Long) {
        val rideRequest = findById(requestId)
        if (rideRequest.ride.availableSeats >=  rideRequest.requestedSeats) {
            repository.updateRideRequestStatus(requestId = requestId, status = RideRequestStatus.APPROVED)
            tripService.updateRideAvailableSeats(rideId = rideRequest.ride.id, seats = rideRequest.ride.availableSeats - rideRequest.requestedSeats)
        } else throw RuntimeException("Not enough seats available to approve RideRequest with ID: [$requestId]!")
        pushNotification(rideRequest, NotificationType.REQUEST_APPROVED)
    }

    private fun changeRequestToDenied(requestId: Long) {
        repository.updateRideRequestStatus(requestId = requestId, status = RideRequestStatus.DENIED)
        pushNotification(findById(requestId), NotificationType.REQUEST_DENIED)
    }

    private fun changeRequestToCancelled(requestId: Long) {
        val rideRequest = findById(requestId)
        tripService.updateRideAvailableSeats(rideId = rideRequest.ride.id, seats = rideRequest.ride.availableSeats + rideRequest.requestedSeats)
        repository.updateRideRequestStatus(requestId = requestId, status = RideRequestStatus.CANCELLED)
        pushNotification(findById(requestId), NotificationType.REQUEST_CANCELLED)
    }

    private fun changeRequestToRideCancelled(requestId: Long) {
        repository.updateRideRequestStatus(requestId = requestId, status = RideRequestStatus.RIDE_CANCELLED)
        pushNotification(findById(requestId), NotificationType.RIDE_CANCELLED)
    }

    private fun changeRequestToExpired(requestId: Long) {
        repository.updateRideRequestStatus(requestId = requestId, status = RideRequestStatus.EXPIRED)
        pushNotification(findById(requestId), NotificationType.REQUEST_EXPIRED)
    }

    private fun changeRequestToPending(requestId: Long) {
        repository.updateRideRequestStatus(requestId = requestId, status = RideRequestStatus.PENDING)
        pushNotification(findById(requestId), NotificationType.REQUEST_SENT)
    }

    private fun updateStatusIfPossible(requestId: Long, previousStatus: RideRequestStatus,
                                       newStatus: RideRequestStatus) {
        logger.debug("[RideRequestService] Checking if ride request status transition is valid..")
        val request = repository.findById(requestId)
        if (changeStatusActionAllowed(previousStatus, newStatus) && request.get().ride.status == RideStatus.ACTIVE) {
            logger.debug("[RideRequestService]Ride request status transition from $previousStatus to $newStatus is VALID, changing status..")
            when (newStatus) {
                RideRequestStatus.APPROVED -> changeRequestToApproved(requestId)
                RideRequestStatus.DENIED -> changeRequestToDenied(requestId)
                RideRequestStatus.PENDING -> changeRequestToPending(requestId)
                RideRequestStatus.CANCELLED -> changeRequestToCancelled(requestId)
                RideRequestStatus.RIDE_CANCELLED -> changeRequestToRideCancelled(requestId)
                RideRequestStatus.EXPIRED -> changeRequestToExpired(requestId)
            }
        } else throw RuntimeException(
                "Status change not allowed from $previousStatus to $newStatus for RideRequest ID: [$requestId]")
    }

    private fun canSubmitRating(rideRequest: RideRequest): Boolean =
            rideRequest.rating != null && rideRequest.ride.status == RideStatus.FINISHED
                    && rideRequest.status == RideRequestStatus.APPROVED

    private fun checkIsTripActive(tripId: Long): Boolean {
        return this.tripService.findById(tripId).status == RideStatus.ACTIVE
    }

    private fun changeStatusActionAllowed(previousStatus: RideRequestStatus, nextStatus: RideRequestStatus): Boolean {
        if (previousStatus != nextStatus) {
            return when (previousStatus) {
                RideRequestStatus.APPROVED -> nextStatus == RideRequestStatus.CANCELLED
                RideRequestStatus.PENDING -> nextStatus != RideRequestStatus.PENDING
                RideRequestStatus.CANCELLED -> false
                RideRequestStatus.DENIED -> false
                RideRequestStatus.RIDE_CANCELLED -> false
                RideRequestStatus.EXPIRED -> false
            }
        }
        return false
    }

    private fun pushNotification(rideRequest: RideRequest, notificationType: NotificationType) {
        logger.debug("[RideRequestService] Pushing RideRequest Status Notification..")
        notificationService.pushRideRequestStatusChangeNotification(rideRequest = rideRequest, notificationType = notificationType)
    }

    fun rideRequestCronJob(rideRequest: RideRequest) {
        logger.debug("[RideRequestService] Updating RideRequest ${rideRequest.id} to EXPIRED..")
        val request = findById(rideRequest.id)
        request.status = RideRequestStatus.EXPIRED
        repository.save(request)
        pushNotification(request, NotificationType.REQUEST_EXPIRED)
    }
}
