package com.project.najdiprevoz.repositories

import com.project.najdiprevoz.domain.Rating
import com.project.najdiprevoz.domain.RideRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface RatingRepository : JpaRepository<Rating, Long> {



    fun findRatingsByRideRequestRide_Id(rideId: Long): List<Rating>?

//    fun findByAuthor_Id(authorId: Long): List<Rating>?

    @Query("""
        SELECT r from Rating r
        JOIN Ride ride 
        ON r.rideRequest.ride = ride
        WHERE ride.driver.id = :driverId
    """)
    fun findRatingsForDriverId(@Param("driverId") driverId: Long): List<Rating>?

    fun findRatingByRideRequest(rideRequest: RideRequest): Rating?
}