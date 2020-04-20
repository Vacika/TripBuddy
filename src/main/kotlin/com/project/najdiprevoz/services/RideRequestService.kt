package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.RideRequest
import com.project.najdiprevoz.enums.NotificationType
import com.project.najdiprevoz.enums.RequestStatus
import com.project.najdiprevoz.enums.RideStatus
import com.project.najdiprevoz.repositories.RideRequestRepository
import com.project.najdiprevoz.web.request.create.CreateRequestForTrip
import com.project.najdiprevoz.web.response.PastTripResponse
import com.project.najdiprevoz.web.response.RideRequestResponse
import javassist.NotFoundException
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import javax.transaction.Transactional

@Service
class RideRequestService(private val repository: RideRequestRepository,
                         private val tripService: TripService,
                         private val userService: UserService,
                         private val notificationService: NotificationService) {

    fun findRequestById(id: Long): RideRequestResponse = findById(id).mapToRideRequestResponse()

    fun findById(id: Long): RideRequest =
            repository.findById(id)
                    .orElseThrow { NotFoundException("Ride request not found!") }

    fun getAllRequestsByTripId(rideId: Long): List<RideRequestResponse> =
            repository.findAllByRideId(rideId).map { it.mapToRideRequestResponse() }

    fun getAllRequestsForUser(username: String) =
            repository.findAllByRequesterUsername(username = username)

    fun getAll(): List<RideRequest> =
            repository.findAll()

    fun getRequestsForRideByStatus(rideId: Long, status: RequestStatus): List<RideRequestResponse> =
            getAll()
                    .filter { it.ride.id == rideId && it.status == status }
                    .map { it.mapToRideRequestResponse() }

    @Modifying
    fun changeStatusByRideRequestId(id: Long, newStatus: RequestStatus) {
        updateStatusIfPossible(requestId = id, previousStatus = findById(id).status, newStatus = newStatus)
    }

    @Modifying
    @Transactional
    fun addNewRideRequest(req: CreateRequestForTrip, username: String) = with(req) {
        checkIfValidRequest(this, username)
        pushNotification(
                repository.save(RideRequest(
                        status = RequestStatus.PENDING,
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
            throw RuntimeException("Trip applied for seat does not have $requestedSeats seats available! Trip ID: [$tripId]")
        }
        if (!isNotTheDriverItself(username, tripId)) {
            throw RuntimeException("You can't create a ride request for a ride published by you! Username: [$username], TripID: [$tripId]")
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
            rideRequest.status = RequestStatus.APPROVED
            repository.save(rideRequest)
//            repository.updateRideRequestStatus(requestId = requestId, status = RequestStatus.APPROVED)
            repository.flush()
        } else throw RuntimeException("Not enough seats available to approve RideRequest with ID: [$requestId]!")
        pushNotification(rideRequest, NotificationType.REQUEST_APPROVED)
    }

    private fun changeRequestToDenied(requestId: Long) {
        repository.updateRideRequestStatus(requestId = requestId, status = RequestStatus.DENIED)
        pushNotification(findById(requestId), NotificationType.REQUEST_DENIED)
    }

    private fun changeRequestToCancelled(requestId: Long) {
        notificationService.removeAllNotificationsForRideRequest(requestId)
        repository.updateRideRequestStatus(requestId = requestId, status = RequestStatus.CANCELLED)
    }

    private fun changeRequestToRideCancelled(requestId: Long) {
        repository.updateRideRequestStatus(requestId = requestId, status = RequestStatus.RIDE_CANCELLED)
        pushNotification(findById(requestId), NotificationType.RIDE_CANCELLED)
    }

    private fun changeRequestToExpired(requestId: Long) {
        repository.updateRideRequestStatus(requestId = requestId, status = RequestStatus.EXPIRED)
        pushNotification(findById(requestId), NotificationType.REQUEST_EXPIRED)
    }

    private fun changeRequestToPending(requestId: Long) {
        repository.updateRideRequestStatus(requestId = requestId, status = RequestStatus.PENDING)
        pushNotification(findById(requestId), NotificationType.REQUEST_SENT)
    }

    private fun updateStatusIfPossible(requestId: Long, previousStatus: RequestStatus, newStatus: RequestStatus) {
        if (changeStatusActionAllowed(previousStatus, newStatus)) {
            when (newStatus) {
                RequestStatus.APPROVED -> changeRequestToApproved(requestId)
                RequestStatus.DENIED -> changeRequestToDenied(requestId)
                RequestStatus.PENDING -> changeRequestToPending(requestId)
                RequestStatus.CANCELLED -> changeRequestToCancelled(requestId)
                RequestStatus.RIDE_CANCELLED -> changeRequestToRideCancelled(requestId)
                RequestStatus.EXPIRED -> changeRequestToExpired(requestId)
            }
        } else throw RuntimeException("Status change not allowed from $previousStatus to $newStatus for RideRequest ID: [$requestId]")
    }

    private fun pushNotification(rideRequest: RideRequest, notificationType: NotificationType) {
        notificationService.pushRequestStatusChangeNotification(rideRequest = rideRequest, notificationType = notificationType)
    }

    private fun isTripActive(tripId: Long): Boolean {
        return this.tripService.findById(tripId).status == RideStatus.ACTIVE
    }

    private fun changeStatusActionAllowed(previousStatus: RequestStatus, nextStatus: RequestStatus): Boolean {
        if (previousStatus != nextStatus) {
            return when (previousStatus) {
                RequestStatus.APPROVED -> nextStatus == RequestStatus.CANCELLED
                RequestStatus.PENDING -> nextStatus != RequestStatus.PENDING
                RequestStatus.CANCELLED -> false
                RequestStatus.DENIED -> false
                RequestStatus.RIDE_CANCELLED -> false
                RequestStatus.EXPIRED -> false
            }
        }
        return false
    }

    fun rideRequestCronJob(rideRequest: RideRequest) {
        val request = findById(rideRequest.id)
        request.status = RequestStatus.EXPIRED
        repository.save(request)
        pushNotification(request, NotificationType.REQUEST_EXPIRED)
    }
}