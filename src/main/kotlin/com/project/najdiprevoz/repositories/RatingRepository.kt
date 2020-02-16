package com.project.najdiprevoz.repositories

import com.project.najdiprevoz.domain.Rating
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface RatingRepository : JpaRepository<Rating, Long> {
    fun findRatingsByRideId(rideId: Long): List<Rating>?

    fun findRatingsByAuthorId(authorId: Long): List<Rating>?

    @Query("""
        SELECT r from Rating r 
        JOIN Ride ride 
        ON r.ride = ride
        WHERE ride.driver.id = :driverId
    """)
    fun findRatingsForDriverId(@Param("driverId") driverId: Long): List<Rating>?
}