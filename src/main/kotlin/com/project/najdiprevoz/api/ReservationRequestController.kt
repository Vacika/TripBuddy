package com.project.najdiprevoz.api

import com.project.najdiprevoz.enums.ReservationStatus
import com.project.najdiprevoz.mapper.ReservationRequestMapper
import com.project.najdiprevoz.web.request.create.CreateRequestForTrip
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/reservation-requests")
class ReservationRequestController(private val mapper: ReservationRequestMapper) {

    @PutMapping("/new")
    fun createNewReservationRequest(@RequestBody req: CreateRequestForTrip, principal: Principal) =
            mapper.addNewReservationRequest(req, principal.name)

    @GetMapping("/{requestId}")
    fun getRequestById(@PathVariable("requestId") requestId: Long) =
            mapper.findById(requestId)

    @GetMapping("/received")
    fun getReceivedReservationRequests(principal: Principal) =
            mapper.getReceivedReservationRequests(principal.name)

    @GetMapping("/sent")
    fun getSentReservationRequests(principal: Principal) =
            mapper.getSentReservationRequests(principal.name)

    @GetMapping("/{requestId}/approve")
    fun changeStatusToApproved(@PathVariable("requestId") requestId: Long) =
            mapper.changeStatus(requestId, ReservationStatus.APPROVED)

    @GetMapping("/{requestId}/deny")
    fun changeStatusToDenied(@PathVariable("requestId") requestId: Long) =
            mapper.changeStatus(requestId, ReservationStatus.DENIED)

    @GetMapping("/{requestId}/cancel_reservation")
    fun changeStatusToCancelled(@PathVariable("requestId") requestId: Long) =
            mapper.changeStatus(requestId, ReservationStatus.CANCELLED)

//    @GetMapping("/ride/{rideId}")
//    fun getAllRequestsForRide(@PathVariable("rideId") rideId: Long) =
//            mapper.getAllRequestByTripId(rideId)
//
//    @GetMapping("/ride/{rideId}/pending")
//    fun getPendingRequestsForRide(@PathVariable("rideId") rideId: Long) =
//            service.getRequestsForRideByStatus(rideId, ReservationRequestStatus.PENDING)
//
//    @GetMapping("/ride/{rideId}/approved")
//    fun getApprovedRequestsForRide(@PathVariable("rideId") rideId: Long) =
//            service.getRequestsForRideByStatus(rideId, ReservationRequestStatus.APPROVED)
//
//    @GetMapping("/ride/{rideId}/denied")
//    fun getDeniedRequestsForRide(@PathVariable("rideId") rideId: Long) =
//            service.getRequestsForRideByStatus(rideId, ReservationRequestStatus.DENIED)
}