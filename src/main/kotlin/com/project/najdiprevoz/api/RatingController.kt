package com.project.najdiprevoz.api

import com.project.najdiprevoz.services.RatingService
import com.project.najdiprevoz.web.request.create.CreateRatingRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/ratings")
class RatingController(private val service: RatingService) {
    @GetMapping("/member/{memberId}")
    fun getRatingsForMember(@PathVariable("memberId") memberId: Long) =
            service.getRatingsForMember(memberId)

    @PostMapping("/add")
    fun createNewRating(@RequestBody createRatingRequest: CreateRatingRequest) =
            service.addRating(createRatingRequest)

    @GetMapping
    fun getMyRatings(memberId: Long) =
            service.getRatingsForMember(memberId)

    @GetMapping("/ride/{rideId}")
    fun getRatingsForRide(@PathVariable("rideId") rideId: Long) =
            service.getRatingsForRide(rideId)
}
