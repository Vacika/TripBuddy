package com.project.najdiprevoz.api

import com.project.najdiprevoz.domain.Ride
import com.project.najdiprevoz.services.TripService
import com.project.najdiprevoz.web.request.FilterTripRequest
import com.project.najdiprevoz.web.request.create.CreateTripRequest
import com.project.najdiprevoz.web.request.edit.EditTripRequest
import org.springframework.web.bind.annotation.*
import javax.annotation.PostConstruct

@RestController
@RequestMapping("/api/trips")
class TripController(private val service: TripService) {

    @GetMapping
    fun getAllActiveTrips() =
            service.findAllActiveTripsWithAvailableSeats()

    @GetMapping
    fun findAllFiltered(filterRequest: FilterTripRequest): List<Ride> =
            service.findAllFiltered(filterRequest)


    @GetMapping("/{tripId}")
    fun getTrip(@PathVariable("tripId") tripId: Long) =
            service.findById(tripId)

    @GetMapping("/cancel/{tripId}")
    fun cancelRide(@PathVariable("tripId") tripId: Long) =
            service.deleteRide(tripId)

    @PostMapping("/edit/{tripId}")
    fun editRide(@PathVariable("tripId") tripId: Long, @RequestBody editTripRequest: EditTripRequest) =
            service.editRide(tripId, editTripRequest)

    @GetMapping("/finish/{tripId}")
    fun markAsFinished(@PathVariable("tripId") tripId: Long) =
            service.setRideFinished(tripId)

    //TODO: Replace this
    @GetMapping("/history/{userId}")
    fun getUserPastRides(@PathVariable("userId") userId: Long) =
            service.getPastRidesForUser(userId)

    @GetMapping("/{cityFrom}-{cityTo}")
    fun getRidesForRelation(@PathVariable("cityFrom") cityFrom: String,
                            @PathVariable("cityTo") cityTo: String) =
            service.findFromToRides(from = cityFrom, to = cityTo)

    @GetMapping("/edit-seats/{tripId}")
    fun decreaseAvailableSeats(@PathVariable("tripId") tripId: Long, minusSeats: Int) =
            service.decreaseSeatsOffered(tripId, minusSeats)

    @PostMapping("/add")
    fun addNewRide(@RequestBody createTripRequest: CreateTripRequest): Ride =
            service.createNewRide(createTripRequest)

    @PostConstruct
    fun test(): List<Ride> {
        val t = findAllFiltered(FilterTripRequest(fromAddress = "Skopje", toAddress = "Struga", departureTime = null, requestedSeats = null, departureDay = null))
        return t
    }
}