package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.RideRequest
import com.project.najdiprevoz.enums.RequestRideStatus
import com.project.najdiprevoz.repositories.RideRequestRepository
import com.project.najdiprevoz.web.request.edit.ChangeRideRequestStatusRequest
import org.springframework.stereotype.Service

@Service
class RideRequestService(private val repository: RideRequestRepository) {

    fun findAllRequestsForRide(rideId: Long) =
            repository.findAllByRide_Id(rideId)

    fun findAllRequestsForMember(memberId: Long) =
            repository.findAllByRequester_Id(requesterId = memberId)

    private fun getAll(): List<RideRequest> =
            repository.findAll()

    fun getApprovedRideRequestsForRide(rideId: Long) =
            repository.getApprovedRequestsForRide(rideId = rideId)

    fun changeStatus(changeRideRequestStatusRequest: ChangeRideRequestStatusRequest) =
            with(changeRideRequestStatusRequest) {
                repository.updateRideRequestStatus(requestId = requestId, status = status) == 1 // returns row affected by update == 1
            }

    fun isRideRequestFinished(rideRequestId: Long) =
            repository.isRideRequestFinished(rideRequestId = rideRequestId)

    private fun getRequestsForRideByStatus(rideId: Long, status: RequestRideStatus): List<RideRequest> =
            getAll().filter { it.ride.id == rideId && it.status == status }

    fun getDeniedRequestsForRide(rideId: Long) = getRequestsForRideByStatus(rideId, RequestRideStatus.DENIED)

    fun getPendingRequestsForRide(rideId: Long) = getRequestsForRideByStatus(rideId, RequestRideStatus.PENDING)
}