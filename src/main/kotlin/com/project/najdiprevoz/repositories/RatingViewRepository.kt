package com.project.najdiprevoz.repositories

import com.project.najdiprevoz.domain.RatingView
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

//TODO: Remove
@Repository
interface RatingViewRepository: JpaRepository<RatingView, Long> {

    fun findAllByDriverId(id: Long): List<RatingView>
    fun findAllByRideId(rideId: Long): List<RatingView>
}