package com.project.najdiprevoz.repositories

import com.project.najdiprevoz.domain.Ride
import com.project.najdiprevoz.domain.RideRequest
import com.project.najdiprevoz.enums.RequestRideStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
interface RideRequestRepository : JpaRepository<RideRequest, Long>, JpaSpecificationExecutor<RideRequest> {
    fun findAllByRide_Id(rideId: Long): List<RideRequest>

    fun findAllByRequester_Id(requesterId: Long): List<RideRequest>?

    @Query("""
        SELECT rd from RideRequest rd 
        JOIN Ride r 
        ON r = rd.ride
        WHERE r.id = :rideId
        AND rd.status = 'Approved'
    """)
    fun getApprovedRequestsForRide(@Param("rideId") rideId: Long): List<RideRequest>?

    @Modifying
    @Transactional
    @Query("""
        UPDATE RideRequest r
        SET r.status = :status
        WHERE r.id = :requestId
    """)
    fun updateRideRequestStatus(@Param("requestId") requestId: Long, @Param("status") status: RequestRideStatus): Int


    @Query("""
        SELECT r.ride.finished 
        FROM RideRequest r 
        WHERE r.id = :rideRequestId
    """)
    fun isRideRequestFinished(@Param("rideRequestId") rideRequestId: Long): Boolean
}