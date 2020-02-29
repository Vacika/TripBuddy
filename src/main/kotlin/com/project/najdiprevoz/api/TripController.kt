package com.project.najdiprevoz.api

import com.project.najdiprevoz.services.TripService
import com.project.najdiprevoz.web.request.FilterTripRequest
import com.project.najdiprevoz.web.request.create.CreateTripRequest
import com.project.najdiprevoz.web.request.edit.EditTripRequest
import com.project.najdiprevoz.web.response.TripResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/trips")
class TripController(private val service: TripService) {

    @GetMapping
    fun getAllActiveTrips(): List<TripResponse> =
            service.findAllActiveTripsWithAvailableSeats()

    @GetMapping("/filter")
    fun findAllFiltered(filterRequest: FilterTripRequest) =
            service.findAllFiltered(filterRequest)

    @GetMapping("/{tripId}")
    fun getTrip(@PathVariable("tripId") tripId: Long) =
            service.findById(tripId)

    @PostMapping("/add")
    fun addNewTrip(@RequestBody createTripRequest: CreateTripRequest) =
            service.createNewTrip(createTripRequest)

    @PostMapping("/edit/{tripId}")
    fun editTrip(@PathVariable("tripId") tripId: Long, @RequestBody req: EditTripRequest) =
            service.editTrip(tripId, req)

    @GetMapping("/cancel/{tripId}")
    fun cancelTrip(@PathVariable("tripId") tripId: Long) =
            service.cancelTrip(tripId)

    @GetMapping("/history/{userId}")
    fun getUserPastTrips(@PathVariable("userId") userId: Long) =
            service.getPastTripsForUser(userId)

    @GetMapping("/all/{userId}")
    fun getAllUserTrips(@PathVariable("userId") userId: Long) =
            service.getAllTripsForUser(userId)

    @GetMapping("/{cityFrom}/{cityTo}")
    fun getTripsForRelation(@PathVariable("cityFrom") cityFrom: String,
                            @PathVariable("cityTo") cityTo: String) =
            service.findRidesByFromLocationAndDestination(from = cityFrom, to = cityTo)

//    @GetMapping("/finish/{tripId}")
//    fun markAsFinished(@PathVariable("tripId") tripId: Long) =
//            service.setRideFinished(tripId)

//    @GetMapping("/edit-seats/{tripId}")
//    fun decreaseAvailableSeats(@PathVariable("tripId") tripId: Long, minusSeats: Int) =
//            service.decreaseSeatsOffered(tripId, minusSeats)
}