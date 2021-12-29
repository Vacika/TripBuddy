package com.project.najdiprevoz.services.list

import com.project.najdiprevoz.domain.Rating
import com.project.najdiprevoz.repositories.RatingRepository
import org.springframework.stereotype.Service

@Service
class RatingListService(private val repository: RatingRepository) {

    fun getRatingsForTrip(tripId: Long) =
        repository.findRatingsByReservationRequestTrip_Id(tripId = tripId)
            .sortedByDescending { it.dateSubmitted }

    fun getRatingsForUser(username: String) =
        repository.findAllByRatedUser_Username(username).map { it.mapToRatingResponse() }
            .sortedByDescending { it.tripDate }


    fun getRatingsForUserById(userId: Long): List<Rating> {
        return repository.findAllByRatedUser_Id(userId)
            .sortedByDescending { it.dateSubmitted }
    }
}