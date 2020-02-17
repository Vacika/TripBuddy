package com.project.najdiprevoz.repositories

import com.project.najdiprevoz.domain.Member
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : JpaRepository<Member, Long> {

    @Query("""
        SELECT u as rating FROM Member u
        JOIN Rating r on
        r.rideRequest.ride.driver = u
        GROUP BY u.id
        ORDER BY avg(rating)
        DESC
    """)
    fun findTopRatedDrivers(page: Pageable): List<Member>?
}