package com.project.najdiprevoz.api

import com.project.najdiprevoz.services.TripService
import com.project.najdiprevoz.web.request.create.CreateTripRequest
import com.project.najdiprevoz.web.request.edit.EditTripRequest
import com.project.najdiprevoz.web.response.PastTripResponse
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/trips")
class TripController(private val service: TripService) {

    @PutMapping("/add")
    fun addNewTrip(@RequestBody createTripRequest: CreateTripRequest, principal: Principal) =
            service.createNewTrip(createTripRequest, principal.name)

    @PostMapping("/edit/{tripId}")
    fun editTrip(@PathVariable("tripId") tripId: Long, @RequestBody req: EditTripRequest) =
            service.editTrip(tripId, req)

    @GetMapping("/cancel/{tripId}")
    fun cancelTrip(@PathVariable("tripId") tripId: Long) =
            service.cancelTrip(tripId)

    @GetMapping("/history/{userId}")
    fun getUserTripsWhereHeIsDriver(@PathVariable("userId") userId: Long) =
            service.getPastPublishedTripsByUser(userId)

    @GetMapping("/all/{userId}")
    fun getAllUserTrips(@PathVariable("userId") userId: Long) =
            service.getAllTripsForUser(userId)

    @GetMapping("/history/past-trips")
    fun getPastTrips(principal: Principal): List<PastTripResponse> {
        return service.getMyPastTrips(username = principal.name)
    }
}