package com.project.najdiprevoz.repositories

import com.project.najdiprevoz.domain.ReservationRequest
import com.project.najdiprevoz.enums.ReservationStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
interface ReservationRequestRepository : JpaRepository<ReservationRequest, Long>, JpaSpecificationExecutor<ReservationRequest> {
    fun findAllByTripId(tripId: Long): List<ReservationRequest>

    fun findAllByRequesterUsername(username: String): List<ReservationRequest>

    @Query("SELECT r from ReservationRequest r where r.trip.driver.username = :username")
    fun findReceivedRequests(@Param("username") username:String): List<ReservationRequest>

    @Transactional
    @Modifying
    @Query("""
        UPDATE ReservationRequest r
        SET r.status = :status
        WHERE r.id = :requestId
    """)
    fun updateReservationRequestStatus(@Param("requestId") requestId: Long, @Param("status") status: ReservationStatus): Int

    fun findAllByStatusAndTripId(status: ReservationStatus, tripId:Long): List<ReservationRequest>

    fun findByTripIdAndRequesterUsername(tripId: Long, username: String): List<ReservationRequest>


//    @Query("""
//        SELECT r.trip.status
//        FROM ReservationRequest r
//        WHERE r.id = :reservationRequestId
//    """)
//    fun getRideStatus(@Param("reservationRequestId") ReservationRequestId: Long): RideStatus
//
//
//    @Modifying
//    @Transactional
//    @Query("""UPDATE ReservationRequest r set r.status='TRIP_CANCELLED' where r.status='PENDING' and r.trip.status='FINISHED'""")
//    fun updateReservationRequestsCron(): Int
}