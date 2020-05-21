package com.project.najdiprevoz.api

import com.project.najdiprevoz.enums.RideRequestStatus
import com.project.najdiprevoz.services.RideRequestService
import com.project.najdiprevoz.web.request.create.CreateRequestForTrip
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/ride-requests")
class RideRequestController(private val service: RideRequestService) {

    @PutMapping("/new")
    fun createNewRideRequest(@RequestBody req: CreateRequestForTrip, principal: Principal) =
            service.addNewRideRequest(req, principal.name)



    @GetMapping("/ride/{rideId}")
    fun getAllRequestsForRide(@PathVariable("rideId") rideId: Long) =
            service.getAllRequestsByTripId(rideId)

    @GetMapping("/{requestId}")
    fun getRequestById(@PathVariable("requestId") requestId: Long) =
            service.findByIdMappedToResponse(requestId)


    @GetMapping("/received")
    fun getReceivedRideRequests(principal: Principal) =
            service.getReceivedRideRequests(principal.name)

    @GetMapping("/sent")
    fun getSentRideRequests(principal: Principal) =
            service.getSentRideRequests(principal.name)




    @GetMapping("/ride/{rideId}/pending")
    fun getPendingRequestsForRide(@PathVariable("rideId") rideId: Long) =
            service.getRequestsForRideByStatus(rideId, RideRequestStatus.PENDING)

    @GetMapping("/ride/{rideId}/approved")
    fun getApprovedRequestsForRide(@PathVariable("rideId") rideId: Long) =
            service.getRequestsForRideByStatus(rideId, RideRequestStatus.APPROVED)

    @GetMapping("/ride/{rideId}/denied")
    fun getDeniedRequestsForRide(@PathVariable("rideId") rideId: Long) =
            service.getRequestsForRideByStatus(rideId, RideRequestStatus.DENIED)




    @GetMapping("/{requestId}/approve")
    fun changeStatusToApproved(@PathVariable("requestId") requestId: Long) =
            service.changeStatus(requestId, RideRequestStatus.APPROVED)

    @GetMapping("/{requestId}/deny")
    fun changeStatusToDenied(@PathVariable("requestId") requestId: Long) =
            service.changeStatus(requestId, RideRequestStatus.DENIED)

    @GetMapping("/{requestId}/cancel")
    fun changeStatusToCancelled(@PathVariable("requestId") requestId: Long) =
            service.changeStatus(requestId, RideRequestStatus.CANCELLED)
}