package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.RideRequest
import com.project.najdiprevoz.enums.RequestStatus
import com.project.najdiprevoz.enums.RideStatus
import com.project.najdiprevoz.repositories.RideRequestRepository
import com.project.najdiprevoz.web.request.create.CreateRequestForRideRequest
import com.project.najdiprevoz.web.request.edit.ChangeRideRequestStatusRequest
import javassist.NotFoundException
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class RideRequestService(private val repository: RideRequestRepository,
                         private val rideService: RideService,
                         private val memberService: MemberService,
                         private val notificationService: NotificationService) {

    fun findById(id: Long) = repository.findById(id).orElseThrow { NotFoundException("Ride request not found!") }

    fun findAllRequestsForRide(rideId: Long) =
            repository.findAllByRideId(rideId)

    fun findAllRequestsForMember(memberId: Long) =
            repository.findAllByRequesterId(requesterId = memberId)

    fun getAll(): List<RideRequest> =
            repository.findAll()

    fun getApprovedRideRequestsForRide(rideId: Long) =
            repository.findApprovedRequestsForRide(rideId = rideId)

    fun changeStatusByRideRequest(rideRequest: RideRequest, status: RequestStatus) {
        repository.updateRideRequestStatus(rideRequest.id, status)
        pushNotification(findById(rideRequest.id))
    }

    fun changeStatusByRideRequestId(rideRequestId: Long, status: RequestStatus) {
        repository.updateRideRequestStatus(rideRequestId, status)
        pushNotification(findById(rideRequestId))
    }

    fun changeStatus(changeRideRequestStatusRequest: ChangeRideRequestStatusRequest) =
            with(changeRideRequestStatusRequest) {
                if (changeStatusActionAllowed(previousStatus, newStatus))
                    repository.updateRideRequestStatus(requestId = requestId, status = newStatus)
                pushNotification(findById(requestId))
            }

    fun addNewRideRequest(createRideRequest: CreateRequestForRideRequest) = with(createRideRequest) {
        pushNotification(repository.save(RideRequest(
                status = RequestStatus.PENDING,
                ride = rideService.findById(rideId),
                createdOn = ZonedDateTime.now(),
                requester = memberService.findMemberById(requesterId))))
    }

    private fun pushNotification(rideRequest: RideRequest) {
        notificationService.pushNotification(rideRequest)
    }

    private fun isRideRequestFinished(rideRequestId: Long) =
            repository.getRideStatus(rideRequestId = rideRequestId) == RideStatus.FINISHED

    private fun getRequestsForRideByStatus(rideId: Long, status: RequestStatus): List<RideRequest> =
            getAll().filter { it.ride.id == rideId && it.status == status }

    private fun changeStatusActionAllowed(previousStatus: RequestStatus, nextStatus: RequestStatus): Boolean {
        if (previousStatus != nextStatus) {
            return when (previousStatus) {
                RequestStatus.APPROVED -> nextStatus == RequestStatus.CANCELLED
                RequestStatus.PENDING -> true
                RequestStatus.CANCELLED -> false
                RequestStatus.DENIED -> false
                RequestStatus.RIDE_CANCELLED -> true
                RequestStatus.EXPIRED -> false
            }
        }
        return false
    }

    fun getDeniedRequestsForRide(rideId: Long) = getRequestsForRideByStatus(rideId, RequestStatus.DENIED)

    fun getPendingRequestsForRide(rideId: Long) = getRequestsForRideByStatus(rideId, RequestStatus.PENDING)
}