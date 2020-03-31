package com.project.najdiprevoz.api

import com.project.najdiprevoz.enums.RequestStatus
import com.project.najdiprevoz.services.RideRequestService
import com.project.najdiprevoz.web.request.create.CreateRequestForTrip
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/ride-requests")
class RideRequestController(private val service: RideRequestService) {

    @PutMapping("/new")
    fun createNewRideRequest(@RequestBody tripId: Long) =
            service.addNewRideRequest(tripId,"email@email.com") // TODO: Does it have to be a requestbody?

    @GetMapping("/ride/{rideId}")
    fun getAllRequestsForRide(@PathVariable("rideId") rideId: Long) =
            service.getAllRequestsByTripId(rideId)

    @GetMapping("/{requestId}")
    fun getRequestById(@PathVariable("requestId") requestId: Long) =
            service.findRequestById(requestId)

    @GetMapping("/ride/{rideId}/pending")
    fun getPendingRequestsForRide(@PathVariable("rideId") rideId: Long) =
            service.getRequestsForRideByStatus(rideId, RequestStatus.PENDING)

    @GetMapping("/ride/{rideId}/approved")
    fun getApprovedRequestsForRide(@PathVariable("rideId") rideId: Long) =
            service.getRequestsForRideByStatus(rideId, RequestStatus.APPROVED)

    @GetMapping("/ride/{rideId}/denied")
    fun getDeniedRequestsForRide(@PathVariable("rideId") rideId: Long) =
            service.getRequestsForRideByStatus(rideId, RequestStatus.DENIED)

    @GetMapping("/my")
    fun findMyRideRequests(principal: Principal) =
            service.getAllRequestsForUser(principal.name)

    //Note: we need the notification id so we can set that notification as SEEN
    //TODO: Get notification id in the request body, not the url.
    @GetMapping("/{notificationId}/{requestId}/approve")
    fun changeStatusToApproved(@PathVariable("requestId") requestId: Long, @PathVariable("notificationId") notificationId: Long) =
            service.changeStatusByRideRequestId(requestId, RequestStatus.APPROVED, notificationId)

    @GetMapping("/{requestId}/deny")
    fun changeStatusToDenied(@PathVariable("requestId") requestId: Long, @PathVariable("notificationId") notificationId: Long) =
            service.changeStatusByRideRequestId(requestId, RequestStatus.DENIED, notificationId)

    @GetMapping("/{requestId}/cancel")
    fun changeStatusToCancelled(@PathVariable("requestId") requestId: Long, @PathVariable("notificationId") notificationId: Long) =
            service.changeStatusByRideRequestId(requestId, RequestStatus.CANCELLED, notificationId)
}