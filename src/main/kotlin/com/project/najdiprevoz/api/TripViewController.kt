package com.project.najdiprevoz.api

import com.project.najdiprevoz.services.TripService
import com.project.najdiprevoz.web.request.FilterTripRequest
import com.project.najdiprevoz.web.response.TripDetailsResponse
import com.project.najdiprevoz.web.response.TripResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/trips-list")
class TripViewController(private val service: TripService) {

    @GetMapping
    fun getAllForToday(): List<TripResponse> =
            service.findAllActiveTripsForToday()

    @GetMapping("/filter")
    fun findAllFiltered(filterRequest: FilterTripRequest) =
            service.findAllFiltered(filterRequest)

    @GetMapping("/{tripId}")
    fun getTrip(@PathVariable("tripId") tripId: Long) =
            service.findByIdMapped(tripId)

    @GetMapping("/{tripId}/info")
    fun getTripAdditionalInfo(@PathVariable("tripId") tripId: Long): TripDetailsResponse = service.getTripAdditionalInfo(tripId)
}