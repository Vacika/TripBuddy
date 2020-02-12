package com.project.najdiprevoz.repositories

import com.project.najdiprevoz.domain.Member
import com.project.najdiprevoz.domain.Rating
import com.project.najdiprevoz.domain.Ride
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RatingRepository : JpaRepository<Rating, Long> {
    fun findRatingsByRide(ride: Ride): List<Rating>?

    fun findRatingsByAuthor(author: Member): List<Rating>?
}