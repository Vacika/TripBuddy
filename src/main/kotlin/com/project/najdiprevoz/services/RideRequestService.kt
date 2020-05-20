package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.RideRequest
import com.project.najdiprevoz.enums.NotificationType
import com.project.najdiprevoz.enums.RideRequestStatus
import com.project.najdiprevoz.enums.RideStatus
import com.project.najdiprevoz.repositories.RideRequestRepository
import com.project.najdiprevoz.web.request.create.CreateRequestForTrip
import com.project.najdiprevoz.web.response.RideRequestFullResponse
import com.project.najdiprevoz.web.response.RideRequestResponse
import javassist.NotFoundException
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.transaction.Transactional

@Service
class RideRequestService(private val repository: RideRequestRepository,
                         private val tripService: TripService,
                         private val userService: UserService,
                         private val notificationService: NotificationService) {

    fun findRequestById(id: Long): RideRequestResponse = findById(id).mapToRideRequestResponse(getAvailableActions(id, true))

    fun findById(id: Long): RideRequest =
            repository.findById(id)
                    .orElseThrow { NotFoundException("Ride request not found!") }

    fun getAllRequestsByTripId(rideId: Long): List<RideRequestResponse> =
            repository.findAllByRideId(rideId).map { it.mapToRideRequestResponse(getAvailableActions(it.id, false)) }

    //Gets request sent from the user
    fun getSentRideRequests(username: String) =
            repository.findAllByRequesterUsername(username = username)
                    .map { mapToRideRequestFullResponse(it, getAvailableActions(it.id, true)) }

    //Gets received requests for user
    fun getReceivedRideRequests(username: String) =
            repository.findReceivedRequests(username = username)
                    .map { mapToRideRequestFullResponse(it, getAvailableActions(it.id, false)) }

    fun getAll(): List<RideRequest> =
            repository.findAll()

    fun getRequestsForRideByStatus(rideId: Long, status: RideRequestStatus): List<RideRequestResponse> =
            getAll()
                    .filter { it.ride.id == rideId && it.status == status }
                    .map { it.mapToRideRequestResponse(getAvailableActions(it.id, true)) }

    @Modifying
    @Transactional
    fun changeStatusByRideRequestId(id: Long, newStatus: RideRequestStatus) {
        updateStatusIfPossible(requestId = id, previousStatus = findById(id).status, newStatus = newStatus)
    }

    @Modifying
    @Transactional
    fun addNewRideRequest(req: CreateRequestForTrip, username: String) = with(req) {
        checkIfValidRequest(this, username)
        pushNotification(
                repository.save(RideRequest(
                        status = RideRequestStatus.PENDING,
                        ride = tripService.findById(tripId),
                        createdOn = ZonedDateTime.now(),
                        requester = userService.findUserByUsername(username),
                        additionalDescription = additionalDescription,
                        requestedSeats = requestedSeats)),
                NotificationType.REQUEST_SENT
        )
    }

    private fun checkIfValidRequest(req: CreateRequestForTrip, username: String) = with(req) {
        if (checkIfAppliedBefore(tripId, username)) {
            throw RuntimeException("User [$username] has already sent a ride request for Trip [$tripId]")
        }
        if (!isTripActive(tripId)) {
            throw RuntimeException("Trip applied for seat is not ACTIVE! Trip ID: [$tripId]")
        }
        if (!checkIfEnoughAvailableSeats(tripId, requestedSeats)) {
            throw RuntimeException(
                    "Trip applied for seat does not have $requestedSeats seats available! Trip ID: [$tripId]")
        }
        if (!isNotTheDriverItself(username, tripId)) {
            throw RuntimeException(
                    "You can't create a ride request for a ride published by you! Username: [$username], TripID: [$tripId]")
        }
    }

    private fun isNotTheDriverItself(username: String, tripId: Long): Boolean =
            this.tripService.findById(tripId).driver.username != username


    private fun checkIfEnoughAvailableSeats(tripId: Long, requestedSeats: Int): Boolean =
            this.tripService.findById(tripId).getAvailableSeats() >= requestedSeats

    private fun checkIfAppliedBefore(tripId: Long, username: String): Boolean {
        return repository.findByRideIdAndRequester_Username(tripId, username).isPresent
    }

    private fun changeRequestToApproved(requestId: Long) {
        val rideRequest = findById(requestId)
        if (checkIfEnoughAvailableSeats(rideRequest.ride.id, rideRequest.requestedSeats)) {
            rideRequest.status = RideRequestStatus.APPROVED
            repository.save(rideRequest)
            repository.flush()
        } else throw RuntimeException("Not enough seats available to approve RideRequest with ID: [$requestId]!")
        notificationService.removeLastNotificationForRideRequest(requestId)
        pushNotification(rideRequest, NotificationType.REQUEST_APPROVED)
    }

    private fun changeRequestToDenied(requestId: Long) {
        repository.updateRideRequestStatus(requestId = requestId, status = RideRequestStatus.DENIED)
        notificationService.removeLastNotificationForRideRequest(requestId)
        pushNotification(findById(requestId), NotificationType.REQUEST_DENIED)
    }

    private fun changeRequestToCancelled(requestId: Long) {
        notificationService.removeAllNotificationsForRideRequest(requestId)
        repository.updateRideRequestStatus(requestId = requestId, status = RideRequestStatus.CANCELLED)
        notificationService.pushRequestStatusChangeNotification(findById(requestId), NotificationType.REQUEST_CANCELLED)
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

    private fun updateStatusIfPossible(requestId: Long, previousStatus: RideRequestStatus, newStatus: RideRequestStatus) {
        if (changeStatusActionAllowed(previousStatus, newStatus)) {
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

    private fun getAvailableActions(requestId: Long, forRequester: Boolean): List<String> {
        val currentStatus = findById(requestId).status
        var availableActions = listOf<String>()
        if (!forRequester) {
            if (changeStatusActionAllowed(currentStatus, RideRequestStatus.APPROVED)) availableActions = availableActions.plus(
                    "APPROVE")
            if (changeStatusActionAllowed(currentStatus, RideRequestStatus.DENIED)) availableActions = availableActions.plus(
                    "DENY")
            if (changeStatusActionAllowed(currentStatus, RideRequestStatus.PENDING)) availableActions = availableActions.plus(
                    "UN-APPROVE")
        }
        if (changeStatusActionAllowed(currentStatus, RideRequestStatus.CANCELLED) && forRequester) availableActions = availableActions.plus(
                "CANCEL")
        return availableActions
    }


    private fun pushNotification(rideRequest: RideRequest, notificationType: NotificationType) {
        notificationService.pushRequestStatusChangeNotification(rideRequest = rideRequest,
                notificationType = notificationType)
    }

    private fun isTripActive(tripId: Long): Boolean {
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

    private fun mapToRideRequestFullResponse(rideRequest: RideRequest, allowedActions: List<String>?): RideRequestFullResponse = with(rideRequest) {
        RideRequestFullResponse(
                id = id,
                allowedActions = allowedActions,
                requesterName = requester.getFullName(),
                tripId = ride.id,
                departureTime = ride.departureTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                fromLocation = ride.fromLocation.name,
                toLocation = ride.destination.name,
                driverName = ride.driver.getFullName(),
                requestStatus = status.toString(),
                rideStatus = ride.status.toString()
        )
    }

    fun rideRequestCronJob(rideRequest: RideRequest) {
        val request = findById(rideRequest.id)
        request.status = RideRequestStatus.EXPIRED
        repository.save(request)
        pushNotification(request, NotificationType.REQUEST_EXPIRED)
    }
}