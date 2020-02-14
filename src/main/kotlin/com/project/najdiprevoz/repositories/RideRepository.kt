package com.project.najdiprevoz.repositories

import com.project.najdiprevoz.domain.City
import com.project.najdiprevoz.domain.Member
import com.project.najdiprevoz.domain.Ride
import com.project.najdiprevoz.repositories.projections.AvailableSeatsForRideProjection
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.ZonedDateTime
import javax.transaction.Transactional

@Repository
interface RideRepository : JpaRepository<Ride, Long>, JpaSpecificationExecutor<Ride> {

    fun findAllByDriver(driver: Member): List<Ride>?

    fun findAllByDestination_Name(destination: String): List<Ride>?

    @Query("""SELECT (r.totalSeats - count(rd.id)) as available_seats 
         FROM RideRequest rd 
         JOIN Ride r 
         ON r = rd.ride 
         WHERE rd.status = 'Approved'
         AND r.id = :rideId
         GROUP BY r.id""")
    fun getAvailableSeatsForRide(@Param("rideId") rideId: Long): AvailableSeatsForRideProjection

    fun findAllByFromLocation_Name(name: String): List<Ride>?

    fun findAllByDriver_IdAndFinishedIsTrue(driverId: Long): List<Ride>?

    @Modifying
    @Transactional
    @Query("""
        UPDATE Ride r
        SET r.finished = true
        where r.id = :rideId
    """)
    fun setRideToFinished(@Param("rideId") rideId: Long): Int

    @Modifying
    @Transactional
    @Query("""
        UPDATE Ride r
        SET r.departureTime = :newTime
        where r.id = :rideId
    """)
    fun changeRideTiming(@Param("rideId") rideId: Long, @Param("newTime") newTime: ZonedDateTime): Int
}