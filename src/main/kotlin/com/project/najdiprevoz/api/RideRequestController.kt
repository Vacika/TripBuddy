package com.project.najdiprevoz.api

import com.project.najdiprevoz.services.RideRequestService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/ride-requests")
class RideRequestController(private val service: RideRequestService) {

    @GetMapping("/{rideId}")
    fun getAllRequestsForRide(@PathVariable("rideId") rideId: Long) =
            service.findAllRequestsForRide(rideId)

    @GetMapping("/{rideId}/pending")
    fun getPendingRequestsForRide(@PathVariable("rideId") rideId: Long) =
            service.getPendingRequestsForRide(rideId)

    @GetMapping("/{rideId}/approved")
    fun getApprovedRequestsForRide(@PathVariable("rideId") rideId: Long) =
            service.getApprovedRideRequestsForRide(rideId)

    @GetMapping("/{rideId}/denied")
    fun getDeniedRequestsForRide(@PathVariable("rideId") rideId: Long) =
            service.getDeniedRequestsForRide(rideId)

    @GetMapping("/my/{memberId}")
    fun findRideRequestsByMember(@PathVariable("memberId") memberId: Long) =
            service.findAllRequestsForMember(memberId)

}