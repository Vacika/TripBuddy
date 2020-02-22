package com.project.najdiprevoz.api

import com.project.najdiprevoz.services.RatingService
import com.project.najdiprevoz.web.request.create.CreateRatingRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/ratings")
class RatingController(private val service: RatingService) {
    @GetMapping("/member/{userId}")
    fun getRatingsForMember(@PathVariable("userId") userId: Long) =
            service.getRatingsForMember(userId)

    @PostMapping("/add")
    fun createNewRating(@RequestBody createRatingRequest: CreateRatingRequest) =
            service.addRating(createRatingRequest)

    @GetMapping
    fun getMyRatings(userId: Long) =
            service.getRatingsForMember(userId)

    @GetMapping("/ride/{rideId}")
    fun getRatingsForRide(@PathVariable("rideId") rideId: Long) =
            service.getRatingsForRide(rideId)
}
