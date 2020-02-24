package com.project.najdiprevoz.api

import com.project.najdiprevoz.services.RatingService
import com.project.najdiprevoz.web.request.create.CreateRatingRequest
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/ratings")
class RatingController(private val service: RatingService) {
    @GetMapping("/user") // TODO: should this be path variable or requestbody?
    fun getRatingsForUser(@RequestBody username: String) =
            service.getRatingsForUser(username)

    @PostMapping("/add")
    fun createNewRating(@RequestBody createRatingRequest: CreateRatingRequest) =
            service.addRating(createRatingRequest)

    @GetMapping
    fun getMyRatings(principal: Principal) =
            service.getRatingsForUser(principal.name)

    @GetMapping("/trip/{tripId}")
    fun getRatingsForRide(@PathVariable("tripId") tripId: Long) =
            service.getRatingsForTrip(tripId)
}
