package com.project.najdiprevoz.api

import com.project.najdiprevoz.services.RideService
import com.project.najdiprevoz.web.request.create.CreateRideRequest
import com.project.najdiprevoz.web.request.edit.EditRideRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/rides")
class RideController(private val service: RideService) {

    @GetMapping
    fun getAllActiveRides() =
            service.findAllActiveRidesWithAvailableSeats()

    @GetMapping("/{rideId}")
    fun getRide(@PathVariable("rideId") rideId: Long) =
            service.findById(rideId)

    @GetMapping("/cancel/{rideId}")
    fun cancelRide(@PathVariable("rideId") rideId: Long) =
            service.deleteRide(rideId)

    @PostMapping("/edit/{rideId}")
    fun editRide(@PathVariable("rideId") rideId: Long, @RequestBody editRideRequest: EditRideRequest) =
            service.editRide(rideId, editRideRequest)

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
    fun addNewRide(@RequestBody createRideRequest: CreateRideRequest) =
            service.createNewRide(createRideRequest)
}