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
import javax.annotation.PostConstruct

@Service
class RideRequestService(private val repository: RideRequestRepository,
                         private val rideService: RideService,
                         private val memberService: MemberService,
                         private val notificationService: NotificationService) {

    fun findById(id: Long): RideRequest =
            repository.findById(id)
                    .orElseThrow { NotFoundException("Ride request not found!") }

    fun getAllRequestsForRide(rideId: Long) =
            repository.findAllByRideId(rideId)

    fun getAllRequestsForMember(memberId: Long) =
            repository.findAllByRequesterId(requesterId = memberId)

    fun getAll(): List<RideRequest> =
            repository.findAll()

    fun getApprovedRideRequestsForRide(rideId: Long) =
            repository.findApprovedRequestsForRide(rideId = rideId)

    fun changeStatusByRideRequest(rideRequest: RideRequest, status: RequestStatus) {
        val request = findById(rideRequest.id)
        request.status = status
        repository.save(request)
        pushNotification(request)
    }

    fun changeStatusByRideRequestId(rideRequestId: Long, status: RequestStatus) {
        repository.updateRideRequestStatus(rideRequestId, status)
        pushNotification(findById(rideRequestId))
    }

    fun changeStatus(changeRideRequestStatusRequest: ChangeRideRequestStatusRequest) = with(changeRideRequestStatusRequest) {
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

    fun getDeniedRequestsForRide(rideId: Long) = getRequestsForRideByStatus(rideId, RequestStatus.DENIED)

    fun getPendingRequestsForRide(rideId: Long) = getRequestsForRideByStatus(rideId, RequestStatus.PENDING)


    private fun pushNotification(rideRequest: RideRequest) {
        notificationService.pushRequestStatusChangeNotification(rideRequest)
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

    @PostConstruct
    fun test() {
        changeStatusByRideRequest(findById(1), RequestStatus.EXPIRED)
    }
}