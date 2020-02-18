package com.project.najdiprevoz.api

import com.project.najdiprevoz.enums.RequestStatus
import com.project.najdiprevoz.services.RideRequestService
import com.project.najdiprevoz.web.request.edit.ChangeRideRequestStatusRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/ride-requests")
class RideRequestController(private val service: RideRequestService) {

    @GetMapping("/{rideId}")
    fun getAllRequestsForRide(@PathVariable("rideId") rideId: Long) =
            service.getAllRequestsForRide(rideId)

    @GetMapping("/{rideId}/pending")
    fun getPendingRequestsForRide(@PathVariable("rideId") rideId: Long) =
            service.getPendingRequestsForRide(rideId)

    @GetMapping("/{rideId}/approved")
    fun getApprovedRequestsForRide(@PathVariable("rideId") rideId: Long) =
            service.getApprovedRideRequestsForRide(rideId)

    @GetMapping("/{rideId}/denied")
    fun getDeniedRequestsForRide(@PathVariable("rideId") rideId: Long) =
            service.getDeniedRequestsForRide(rideId)

    @GetMapping("/my}")
    fun findMyRideRequests(principal: Principal) =
            service.getAllRequestsForUser(principal.name)

    @GetMapping("/change-status")
    fun changeStatus(request: ChangeRideRequestStatusRequest) =
            service.changeStatus(request)

    @GetMapping("/approve/{requestId}")
    fun changeStatusToApproved(@PathVariable("requestId") requestId: Long) =
            service.changeStatusByRideRequestId(requestId, RequestStatus.APPROVED)

    @GetMapping("/deny/{requestId}")
    fun changeStatusToDenied(@PathVariable("requestId") requestId: Long) =
            service.changeStatusByRideRequestId(requestId, RequestStatus.DENIED)

    @GetMapping("/cancel/{requestId}")
    fun changeStatusToCancelled(@PathVariable("requestId") requestId: Long) =
            service.changeStatusByRideRequestId(requestId, RequestStatus.CANCELLED)
}