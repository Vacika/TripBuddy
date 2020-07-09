package com.project.najdiprevoz.repositories

import com.project.najdiprevoz.domain.Rating
import com.project.najdiprevoz.domain.RideRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RatingRepository : JpaRepository<Rating, Long> {


    fun findAllByRatedUser_Username(username: String): List<Rating>
    fun findAllByRatedUser_Id(id: String): List<Rating>
    fun findRatingsByRideRequestRide_Id(rideId: Long): List<Rating>

    fun findRatingByRideRequest(rideRequest: RideRequest): Rating?
}