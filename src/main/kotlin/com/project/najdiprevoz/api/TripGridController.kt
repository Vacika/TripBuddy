package com.project.najdiprevoz.api

import com.project.najdiprevoz.mapper.TripMapper
import com.project.najdiprevoz.web.request.FilterTripRequest
import com.project.najdiprevoz.web.response.TripDetailsResponse
import com.project.najdiprevoz.web.response.TripResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/trips-list")
class TripGridController(private val mapper: TripMapper) {

    @GetMapping
    fun getAllForToday(): List<TripResponse> =
            mapper.findAllActiveTripsForToday()

    @GetMapping("/filter")
    fun findAllFiltered(filterRequest: FilterTripRequest) =
            mapper.findAllFiltered(filterRequest)

    @GetMapping("/{tripId}")
    fun getTrip(@PathVariable("tripId") tripId: Long) =
            mapper.findById(tripId)

    @GetMapping("/{tripId}/info")
    fun getTripAdditionalInfo(@PathVariable("tripId") tripId: Long): TripDetailsResponse = mapper.getTripAdditionalInfo(tripId)
}
