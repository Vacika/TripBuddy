package com.project.najdiprevoz.api

import com.project.najdiprevoz.services.RatingService
import com.project.najdiprevoz.web.request.create.CreateRatingRequest
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/ratings")
class RatingController(private val service: RatingService) {

    @GetMapping
    fun getMyRatings(principal: Principal) =
            service.getRatingsForUser(principal.name)

    @GetMapping("/user/{userId}")
    fun getRatingsForUser(@PathVariable("userId") userId: Long) =
            service.getRatingsForUserById(userId).map { it.mapToRatingResponse() }

    @PostMapping("/add")
    fun createNewRating(@RequestBody createRatingRequest: CreateRatingRequest) =
            service.addRating(createRatingRequest)

    @GetMapping("/trip/{tripId}")
    fun getRatingsForRide(@PathVariable("tripId") tripId: Long) =
            service.getRatingsForTrip(tripId)
}
