package com.project.najdiprevoz.api

import com.project.najdiprevoz.mapper.TripMapper
import com.project.najdiprevoz.web.request.create.CreateTripRequest
import com.project.najdiprevoz.web.request.edit.EditTripRequest
import com.project.najdiprevoz.web.response.PastTripResponse
import org.springframework.web.bind.annotation.*
import java.security.Principal
import javax.transaction.Transactional

@RestController
@RequestMapping("/api/trips")
class TripController(private val mapper: TripMapper) {

    @PutMapping("/add")
    fun addNewTrip(@RequestBody createTripRequest: CreateTripRequest, principal: Principal) =
            mapper.createNewTrip(createTripRequest, principal.name)

    @PostMapping("/edit/{tripId}")
    fun editTrip(@PathVariable("tripId") tripId: Long, @RequestBody req: EditTripRequest) =
            mapper.editTrip(tripId, req)

    @Transactional
    @GetMapping("/cancel/{tripId}")
    fun cancelTrip(@PathVariable("tripId") tripId: Long) =
            mapper.cancelTrip(tripId)

    @GetMapping("/my/driver")
    fun getMyTripsAsDriver(principal: Principal) =
            mapper.getMyTripsAsDriver(principal.name)

    @GetMapping("/my/passenger")
    fun getMyTripsAsPassenger(principal: Principal) =
            mapper.getMyTripsAsPassenger(principal.name)

    @GetMapping("/all/{userId}")
    fun getAllUserTrips(@PathVariable("userId") userId: Long) =
            mapper.findAllTripsByDriverId(userId)

    @GetMapping("/history/passenger/past-trips")
    fun findMyPastTripsAsPassenger(principal: Principal): List<PastTripResponse> {
        return mapper.findMyPastTripsAsPassenger(username = principal.name)
    }

    @GetMapping("/history/driver/{userId}")
    fun findMyPastTripsAsDriver(@PathVariable("userId") userId: Long) =
            mapper.getPastPublishedTripsByUser(userId)

//    @GetMapping("/my/driver")
//    fun getMyTripsAsDriver(@RequestParam("page", required=false) page: Int?,
//                           @RequestParam("pageSize",required = false) pageSize: Int? ,
//                           principal: Principal) =
//            mapper.getMyTripsAsDriver(username = principal.name, page = page?:1, pageSize = pageSize?:15)
}