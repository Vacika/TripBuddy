package com.project.najdiprevoz.api

import com.project.najdiprevoz.enums.ReservationStatus
import com.project.najdiprevoz.mapper.ReservationRequestMapper
import com.project.najdiprevoz.web.request.create.CreateReservationRequestForTrip
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/reservation-requests")
class ReservationRequestController(private val mapper: ReservationRequestMapper) {

    @PutMapping("/new")
    fun createNewReservationRequest(@RequestBody req: CreateReservationRequestForTrip, principal: Principal) =
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

//    @GetMapping("/ride/{tripId}")
//    fun getAllRequestsForRide(@PathVariable("tripId") tripId: Long) =
//            mapper.getAllRequestByTripId(tripId)
//
//    @GetMapping("/ride/{tripId}/pending")
//    fun getPendingRequestsForRide(@PathVariable("tripId") tripId: Long) =
//            service.getRequestsForRideByStatus(tripId, ReservationRequestStatus.PENDING)
//
//    @GetMapping("/ride/{tripId}/approved")
//    fun getApprovedRequestsForRide(@PathVariable("tripId") tripId: Long) =
//            service.getRequestsForRideByStatus(tripId, ReservationRequestStatus.APPROVED)
//
//    @GetMapping("/ride/{tripId}/denied")
//    fun getDeniedRequestsForRide(@PathVariable("tripId") tripId: Long) =
//            service.getRequestsForRideByStatus(tripId, ReservationRequestStatus.DENIED)
}