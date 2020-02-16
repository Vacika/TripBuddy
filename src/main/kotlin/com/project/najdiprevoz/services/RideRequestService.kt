package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.RideRequest
import com.project.najdiprevoz.enums.RequestStatus
import com.project.najdiprevoz.repositories.RideRequestRepository
import com.project.najdiprevoz.web.request.edit.ChangeRideRequestStatusRequest
import org.springframework.stereotype.Service

@Service
class RideRequestService(private val repository: RideRequestRepository) {

    fun findAllRequestsForRide(rideId: Long) =
            repository.findAllByRideId(rideId)

    fun findAllRequestsForMember(memberId: Long) =
            repository.findAllByRequesterId(requesterId = memberId)

    private fun getAll(): List<RideRequest> =
            repository.findAll()

    fun getApprovedRideRequestsForRide(rideId: Long) =
            repository.getApprovedRequestsForRide(rideId = rideId)

    fun changeStatus(changeRideRequestStatusRequest: ChangeRideRequestStatusRequest): Boolean =
            with(changeRideRequestStatusRequest) {
                if (canChangeStatus(previousStatus, newStatus))
                    repository.updateRideRequestStatus(requestId = requestId, status = newStatus) == 1  // returns row affected by update == 1
                else
                    return false
            }

    private fun isRideRequestFinished(rideRequestId: Long) =
            repository.isRideRequestFinished(rideRequestId = rideRequestId)

    private fun getRequestsForRideByStatus(rideId: Long, status: RequestStatus): List<RideRequest> =
            getAll().filter { it.ride.id == rideId && it.status == status }

    private fun canChangeStatus(previousStatus: RequestStatus, nextStatus: RequestStatus): Boolean =
            previousStatus != nextStatus && previousStatus == RequestStatus.PENDING

    fun getDeniedRequestsForRide(rideId: Long) = getRequestsForRideByStatus(rideId, RequestStatus.DENIED)

    fun getPendingRequestsForRide(rideId: Long) = getRequestsForRideByStatus(rideId, RequestStatus.PENDING)
}