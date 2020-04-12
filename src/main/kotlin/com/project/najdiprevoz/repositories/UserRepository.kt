package com.project.najdiprevoz.repositories

import com.project.najdiprevoz.domain.AppUser
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<AppUser, Long> {

    @Query("""
        SELECT u as rating FROM AppUser u
        JOIN Rating r on
        r.rideRequest.ride.driver = u
        GROUP BY u.id
        ORDER BY avg(rating)
        DESC
    """)
    fun findTopRatedDrivers(page: Pageable): List<AppUser>?

    fun findByUsername(username: String): Optional<AppUser>

}