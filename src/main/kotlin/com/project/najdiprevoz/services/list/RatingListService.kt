package com.project.najdiprevoz.services.list

import com.project.najdiprevoz.domain.Rating
import com.project.najdiprevoz.repositories.RatingRepository
import org.springframework.stereotype.Service

@Service
class RatingListService(private val repository: RatingRepository) {

    fun getRatingsForTrip(tripId: Long) =
        repository.findRatingsByReservationRequestTrip_Id(tripId = tripId)

    fun getRatingsForUser(username: String) =
        repository.findAllByRatedUser_Username(username).map { it.mapToRatingResponse() }

    fun getRatingsForUserById(userId: Long): List<Rating> {
        return repository.findAllByRatedUser_Id(userId)
    }
}