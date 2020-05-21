package com.project.najdiprevoz.api

import com.project.najdiprevoz.services.TripService
import com.project.najdiprevoz.web.request.create.CreateTripRequest
import com.project.najdiprevoz.web.request.edit.EditTripRequest
import com.project.najdiprevoz.web.response.PastTripResponse
import org.springframework.web.bind.annotation.*
import java.security.Principal
import javax.transaction.Transactional

@RestController
@RequestMapping("/api/trips")
class TripController(private val service: TripService) {

    @PutMapping("/add")
    fun addNewTrip(@RequestBody createTripRequest: CreateTripRequest, principal: Principal) =
            service.createNewTrip(createTripRequest, principal.name)

    @PostMapping("/edit/{tripId}")
    fun editTrip(@PathVariable("tripId") tripId: Long, @RequestBody req: EditTripRequest) =
            service.editTrip(tripId, req)

    @Transactional
    @GetMapping("/cancel/{tripId}")
    fun cancelTrip(@PathVariable("tripId") tripId: Long) =
            service.cancelTrip(tripId)

    @GetMapping("/my/driver")
    fun getMyTripsAsDriver(principal: Principal) =
            service.getMyTripsAsDriver(principal.name)

    @GetMapping("/my/passenger")
    fun getMyTripsAsPassenger(principal: Principal) =
            service.getMyTripsAsPassenger(principal.name)

    @GetMapping("/all/{userId}")
    fun getAllUserTrips(@PathVariable("userId") userId: Long) =
            service.getAllTripsForUser(userId)

    @GetMapping("/history/passenger/past-trips")
    fun findMyPastTripsAsPassenger(principal: Principal): List<PastTripResponse> {
        return service.findMyPastTripsAsPassenger(username = principal.name)
    }

    @GetMapping("/history/driver/{userId}")
    fun findMyPastTripsAsDriver(@PathVariable("userId") userId: Long) =
            service.getPastPublishedTripsByUser(userId)
}