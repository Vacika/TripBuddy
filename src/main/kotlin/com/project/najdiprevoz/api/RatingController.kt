package com.project.najdiprevoz.api

import com.project.najdiprevoz.mapper.RatingMapper
import com.project.najdiprevoz.web.request.create.CreateRatingRequest
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/ratings")
class RatingController(
    private val mapper: RatingMapper
) {

    @GetMapping
    fun getMyRatings(principal: Principal) =
        mapper.getMyRatings(principal)

    @GetMapping("/user/{userId}")
    fun getRatingsForUser(@PathVariable("userId") userId: Long) =
        mapper.getRatingsForUser(userId)

    @PostMapping("/add")
    fun createNewRating(@RequestBody createRatingRequest: CreateRatingRequest) =
        mapper.createNewRating(createRatingRequest)

    @GetMapping("/trip/{tripId}")
    fun getRatingsForTrip(@PathVariable("tripId") tripId: Long) =
        mapper.getRatingsForTrip(tripId)
}
