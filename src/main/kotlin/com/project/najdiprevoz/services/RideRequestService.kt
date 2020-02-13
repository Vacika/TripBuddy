package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.Ride
import com.project.najdiprevoz.domain.RideRequest
import com.project.najdiprevoz.repositories.RideRequestRepository
import com.project.najdiprevoz.web.request.edit.ChangeRideRequestStatusRequest
import org.springframework.stereotype.Service

@Service
class RideRequestService(private val repository: RideRequestRepository) {

    fun findAllRequestsForRide(ride: Ride) =
            repository.findAllByRide(ride)

    fun findAllRequestsForMember(memberId: Long) =
            repository.findAllByRequester_Id(requesterId = memberId)

    fun getAll(): List<RideRequest> =
            repository.findAll()

    fun getApprovedRideRequestsForRide(rideId: Long) =
            repository.getApprovedRequestsForRide(rideId = rideId)

    fun changeStatus(changeRideRequestStatusRequest: ChangeRideRequestStatusRequest) = with(changeRideRequestStatusRequest) {
        repository.updateRideRequestStatus(requestId = requestId, status = status) == 1 // returns row affected by update == 1
    }

    fun isRideRequestFinished(rideRequestId: Long) =
            repository.isRideRequestFinished(rideRequestId = rideRequestId)

}