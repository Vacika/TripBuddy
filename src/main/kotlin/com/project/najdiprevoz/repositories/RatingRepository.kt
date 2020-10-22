package com.project.najdiprevoz.repositories

import com.project.najdiprevoz.domain.Rating
import com.project.najdiprevoz.domain.RideRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface RatingRepository : JpaRepository<Rating, Long> {

    @Query("SELECT r from Rating r join User u on r.rideRequest.ride.driver.username = u.username where u.username=:username")
    fun findAllByRatedUser_Username(@Param("username")username: String): List<Rating>

    @Query("SELECT r from Rating r join User u on r.rideRequest.ride.driver.id = u.id where u.id = :id")
    fun findAllByRatedUser_Id(@Param("id")id: Long): List<Rating>

    fun findRatingsByRideRequestRide_Id(rideId: Long): List<Rating>
}