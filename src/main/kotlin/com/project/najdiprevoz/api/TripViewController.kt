package com.project.najdiprevoz.api

import com.project.najdiprevoz.services.TripService
import com.project.najdiprevoz.web.request.FilterTripRequest
import com.project.najdiprevoz.web.request.create.CreateTripRequest
import com.project.najdiprevoz.web.request.edit.EditTripRequest
import com.project.najdiprevoz.web.response.TripDetailsResponse
import com.project.najdiprevoz.web.response.TripResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/trips")
class TripViewController(private val service: TripService) {

    @GetMapping
    fun getAllForToday(): List<TripResponse> =
            service.findAllActiveTripsForToday()

    @GetMapping("/filter")
    fun findAllFiltered(filterRequest: FilterTripRequest) =
            service.findAllFiltered(filterRequest)

    @GetMapping("/{tripId}")
    fun getTrip(@PathVariable("tripId") tripId: Long) =
            service.findById(tripId)

    @GetMapping("/{tripId}/additional-info")
    fun getTripAdditionalInfo(@PathVariable("tripId") tripId: Long): TripDetailsResponse = service.getTripAdditionalInfo(tripId)

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