package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.RideRequest
import com.project.najdiprevoz.enums.RequestStatus
import com.project.najdiprevoz.enums.RideStatus
import com.project.najdiprevoz.repositories.RideRequestRepository
import com.project.najdiprevoz.web.request.create.CreateRequestForTrip
import com.project.najdiprevoz.web.response.RideRequestResponse
import javassist.NotFoundException
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class RideRequestService(private val repository: RideRequestRepository,
                         private val tripService: TripService,
                         private val userService: UserService,
                         private val notificationService: NotificationService) {

    fun findRequestById(id: Long): RideRequestResponse = convertToRideRequestResponse(findById(id))

    fun findById(id: Long): RideRequest =
            repository.findById(id)
                    .orElseThrow { NotFoundException("Ride request not found!") }

    fun getAllRequestsByTripId(rideId: Long): List<RideRequestResponse> =
            repository.findAllByRideId(rideId).map { convertToRideRequestResponse(it) }

    fun getAllRequestsForUser(username: String) =
            repository.findAllByRequesterUsername(username = username)

    fun getAll(): List<RideRequest> =
            repository.findAll()

    fun getRequestsForRideByStatus(rideId: Long, status: RequestStatus): List<RideRequestResponse> =
            getAll()
                    .filter { it.ride.id == rideId && it.status == status }
                    .map { convertToRideRequestResponse(it) }

    fun changeStatusByRideRequestId(id: Long, newStatus: RequestStatus) {
        updateStatusIfPossible(requestId = id, previousStatus = findById(id).status, newStatus = newStatus)
        notificationService.markAsSeenByRequestId(id)
//        notificationService.markAsSeen(notificationId) // mark previous notification as SEEN
    }

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

    private fun updateStatusIfPossible(requestId: Long, previousStatus: RequestStatus, newStatus: RequestStatus) {
        if (changeStatusActionAllowed(previousStatus, newStatus))
            if (newStatus == RequestStatus.APPROVED) {
                val rideRequest = findById(requestId)
                if (checkIfEnoughAvailableSeats(rideRequest.ride.id, rideRequest.requestedSeats)) {
                    repository.updateRideRequestStatus(requestId = requestId, status = newStatus)
                } else throw RuntimeException("Not enough seats available to approve RideRequest with ID: [$requestId]!")
            }
        pushNotification(findById(requestId))
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
                RequestStatus.PENDING -> TODO()
                RequestStatus.CANCELLED -> TODO()
                RequestStatus.DENIED -> TODO()
                RequestStatus.RIDE_CANCELLED -> TODO()
                RequestStatus.EXPIRED -> TODO()
            }
        }
        return false
    }

    private fun convertToRideRequestResponse(rr: RideRequest): RideRequestResponse = rr.mapToRideRequestResponse()

    fun rideRequestCronJob(rideRequest: RideRequest, status: RequestStatus) {
        val request = findById(rideRequest.id)
        request.status = status
        repository.save(request)
        pushNotification(request)
    }
}