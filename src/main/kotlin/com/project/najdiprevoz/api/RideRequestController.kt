package com.project.najdiprevoz.api

import com.project.najdiprevoz.enums.RequestStatus
import com.project.najdiprevoz.services.RideRequestService
import com.project.najdiprevoz.web.request.create.CreateRequestForTrip
import com.project.najdiprevoz.web.request.edit.ChangeRideRequestStatusRequest
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/ride-requests")
class RideRequestController(private val service: RideRequestService) {

    @GetMapping("/new")
    fun createNewRideRequest(@RequestBody req: CreateRequestForTrip, principal: Principal) =
            service.addNewRideRequest(req, principal.name) // TODO: Does it have to be a requestbody?

    @GetMapping("/ride/{rideId}")
    fun getAllRequestsForRide(@PathVariable("rideId") rideId: Long) =
            service.getAllRequestsByTripId(rideId)

    @GetMapping("/ride/{rideId}/pending")
    fun getPendingRequestsForRide(@PathVariable("rideId") rideId: Long) =
            service.getPendingRequestsForRide(rideId)

    @GetMapping("/{requestId}")
    fun getRequest(@PathVariable("requestId") requestId: Long) =
            service.findById(requestId)

    @GetMapping("/ride/{rideId}/approved")
    fun getApprovedRequestsForRide(@PathVariable("rideId") rideId: Long) =
            service.getApprovedRideRequestsForTrip(rideId)

    @GetMapping("/ride/{rideId}/denied")
    fun getDeniedRequestsForRide(@PathVariable("rideId") rideId: Long) =
            service.getDeniedRequestsForRide(rideId)

    @GetMapping("/my}")
    fun findMyRideRequests(principal: Principal) =
            service.getAllRequestsForUser(principal.name)

    @GetMapping("/change-status")
    fun changeStatus(request: ChangeRideRequestStatusRequest) =
            service.changeRideRequestStatus(request)

    @GetMapping("/{requestId}/approve")
    fun changeStatusToApproved(@PathVariable("requestId") requestId: Long) =
            service.changeStatusByRideRequestId(requestId, RequestStatus.APPROVED)

    @GetMapping("/{requestId}/deny")
    fun changeStatusToDenied(@PathVariable("requestId") requestId: Long) =
            service.changeStatusByRideRequestId(requestId, RequestStatus.DENIED)

    @GetMapping("/{requestId}/cancel")
    fun changeStatusToCancelled(@PathVariable("requestId") requestId: Long) =
            service.changeStatusByRideRequestId(requestId, RequestStatus.CANCELLED)
}