package com.project.najdiprevoz.repositories

import com.project.najdiprevoz.domain.Member
import com.project.najdiprevoz.domain.Ride
import com.project.najdiprevoz.domain.RideRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface RideRequestRepository : JpaRepository<RideRequest, Long>, JpaSpecificationExecutor<RideRequest> {
    fun findAllByRide(ride: Ride): List<RideRequest>?

    fun findAllByRequester(requester: Member): List<RideRequest>?

    @Query("""
        SELECT rd from RideRequest rd 
        JOIN Ride r 
        ON r = rd.ride
        WHERE r = :ride
        AND rd.status = 'Approved'
    """)
    fun getApprovedRequestsForRide(@Param("ride") ride: Ride): List<RideRequest>?
}