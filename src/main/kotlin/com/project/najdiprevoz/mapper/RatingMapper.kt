package com.project.najdiprevoz.mapper

import com.project.najdiprevoz.services.RatingService
import com.project.najdiprevoz.services.list.RatingListService
import com.project.najdiprevoz.web.request.create.CreateRatingRequest
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import java.security.Principal

@Service
class RatingMapper(
    private val service: RatingService,
    private val ratingListService: RatingListService
) {

    fun getMyRatings(principal: Principal) =
        ratingListService.getRatingsForUser(principal.name)

    fun getRatingsForUser(@PathVariable("userId") userId: Long) =
        ratingListService.getRatingsForUserById(userId).map { it.mapToRatingResponse() }

    fun createNewRating(@RequestBody createRatingRequest: CreateRatingRequest) =
        service.addRating(createRatingRequest)

    fun getRatingsForTrip(@PathVariable("tripId") tripId: Long) =
        ratingListService.getRatingsForTrip(tripId)
}