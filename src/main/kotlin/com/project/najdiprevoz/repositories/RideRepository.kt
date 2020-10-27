package com.project.najdiprevoz.repositories

import com.project.najdiprevoz.domain.Rating
import com.project.najdiprevoz.domain.Trip
import com.project.najdiprevoz.enums.TripStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.ZonedDateTime
import javax.transaction.Transactional

@Repository
interface RideRepository : JpaRepository<Trip, Long>, JpaSpecificationExecutor<Trip> {

    fun findAllByDriverId(driverId: Long): List<Trip>

    fun findAllByDriverIdAndStatus(driverId: Long, status: TripStatus): List<Trip>

    @Query("SELECT r from Trip r JOIN ReservationRequest rr on rr.trip = r where rr.requester.username=:username and rr.status='APPROVED' and r.status='FINISHED'")
    fun findMyPastTripsAsPassenger(@Param("username") username: String): List<Trip>

    @Query("SELECT r FROM Rating r JOIN ReservationRequest rr ON r.reservationRequest=rr WHERE rr.requester.username=:username and rr.trip=:trip")
    fun canSubmitRating(@Param("username") username: String, @Param("trip") trip: Trip): List<Rating>

    @Modifying
    @Transactional
    @Query("""UPDATE Trip r 
        SET r.status = 'FINISHED'
        WHERE r.departureTime < :dateTimeNow
        AND r.status = 'ACTIVE' """)
    fun updateFinishedTripsCron(@Param("dateTimeNow") dateTimeNow: ZonedDateTime): Int

    @Modifying
    @Transactional
    @Query("""UPDATE Trip r SET r.status = :status where r.id = :tripId""")
    fun changeTripStatus(@Param("tripId") tripId: Long, @Param("status") status: TripStatus): Int

    fun findAllByDriverUsername(username: String): List<Trip>

    @Query("SELECT r from Trip r JOIN ReservationRequest rr on rr.trip = r where rr.requester.username=:username and rr.status='APPROVED'")
    fun findAllMyTripsAsPassenger(@Param("username") username: String): List<Trip>

    @Modifying
    @Query("UPDATE Trip set availableSeats = :seats where id = :tripId")
    fun updateTripAvailableSeats(@Param("tripId") tripId: Long, @Param("seats") seats: Int)

}