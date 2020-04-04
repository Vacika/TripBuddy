package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.RideRequest
import com.project.najdiprevoz.enums.RequestStatus
import com.project.najdiprevoz.enums.RideStatus
import com.project.najdiprevoz.repositories.RideRequestRepository
import com.project.najdiprevoz.web.request.create.CreateRequestForTrip
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
                        requestedSeats = requestedSeats))
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
    }

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
        pushNotification(rideRequest)
    }

    private fun changeRequestToDenied(requestId: Long) {
        repository.updateRideRequestStatus(requestId = requestId, status = RequestStatus.DENIED)
        pushNotification(findById(requestId))
    }

    private fun changeRequestToCancelled(requestId: Long) {
        notificationService.removeAllNotificationsForRideRequest(requestId)
        repository.updateRideRequestStatus(requestId = requestId, status = RequestStatus.CANCELLED)
    }

    private fun changeRequestToRideCancelled(requestId: Long) {
        repository.updateRideRequestStatus(requestId = requestId, status = RequestStatus.RIDE_CANCELLED)
        pushNotification(findById(requestId))
    }

    private fun changeRequestToExpired(requestId: Long) {
        repository.updateRideRequestStatus(requestId = requestId, status = RequestStatus.EXPIRED)
        pushNotification(findById(requestId))
    }

    private fun changeRequestToPending(requestId: Long) {
        repository.updateRideRequestStatus(requestId = requestId, status = RequestStatus.PENDING)
        pushNotification(findById(requestId))
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
        }
    }

    private fun pushNotification(rideRequest: RideRequest) {
        notificationService.pushRequestStatusChangeNotification(rideRequest = rideRequest)
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

    fun rideRequestCronJob(rideRequest: RideRequest, status: RequestStatus) {
        val request = findById(rideRequest.id)
        request.status = status
        repository.save(request)
        pushNotification(request)
    }
}