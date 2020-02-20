package com.project.najdiprevoz.api

import com.project.najdiprevoz.domain.Ride
import com.project.najdiprevoz.services.TripService
import com.project.najdiprevoz.web.request.FilterTripRequest
import com.project.najdiprevoz.web.request.create.CreateTripRequest
import com.project.najdiprevoz.web.request.edit.EditTripRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/rides")
class TripController(private val service: TripService) {

    @GetMapping
    fun getAllActiveRides() =
            service.findAllActiveRidesWithAvailableSeats()

    @GetMapping
    fun findAllFiltered(filterRequest: FilterTripRequest): List<Ride> =
            service.findAllFiltered(filterRequest)


    @GetMapping("/{rideId}")
    fun getRide(@PathVariable("rideId") rideId: Long) =
            service.findById(rideId)

    @GetMapping("/cancel/{rideId}")
    fun cancelRide(@PathVariable("rideId") rideId: Long) =
            service.deleteRide(rideId)

    @PostMapping("/edit/{rideId}")
    fun editRide(@PathVariable("rideId") rideId: Long, @RequestBody editTripRequest: EditTripRequest) =
            service.editRide(rideId, editTripRequest)

    @GetMapping("/finish/{rideId}")
    fun markAsFinished(@PathVariable("rideId") rideId: Long) =
            service.setRideFinished(rideId)

    //TODO: Replace this
    @GetMapping("/history/{userId}")
    fun getUserPastRides(@PathVariable("userId") userId: Long) =
            service.getPastRidesForUser(userId)

    @GetMapping("/{cityFrom}-{cityTo}")
    fun getRidesForRelation(@PathVariable("cityFrom") cityFrom: String,
                            @PathVariable("cityTo") cityTo: String) =
            service.findFromToRides(from = cityFrom, to = cityTo)

    @GetMapping("/edit-seats/{rideId}")
    fun decreaseAvailableSeats(@PathVariable("rideId") rideId: Long, minusSeats: Int) =
            service.decreaseSeatsOffered(rideId, minusSeats)

    @PostMapping("/add")
    fun addNewRide(@RequestBody createTripRequest: CreateTripRequest): Ride =
            service.createNewRide(createTripRequest)
}