package com.project.najdiprevoz.repositories

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

    fun findAllByFromLocationNameAndDestinationName(fromLocationName: String, destinationName: String): List<Ride>

    fun findAllByDestinationName(destination: String): List<Ride>?

    @Query("""SELECT (r.totalSeatsOffered - count(rd.id)) as available_seats 
         FROM RideRequest rd 
         JOIN Ride r 
         ON r = rd.ride 
         WHERE rd.status = 'APPROVED'
         AND r.id = :rideId
         GROUP BY r.id""")
    fun getAvailableSeatsForRide(@Param("rideId") rideId: Long): AvailableSeatsForRideProjection

    fun findAllByFromLocationName(name: String): List<Ride>?

    fun findRidesByFinishedIsFalse(): List<Ride>

    fun findAllByFinishedIsTrueAndDriverId(driverId: Long): List<Ride>?

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

    @Modifying
    @Transactional
    @Query("""UPDATE Ride r 
        SET r.finished = true 
        WHERE r.departureTime < :dateTimeNow
        AND r.finished = false""")
    fun updateRidesCron(@Param("dateTimeNow") dateTimeNow: ZonedDateTime): Int

}