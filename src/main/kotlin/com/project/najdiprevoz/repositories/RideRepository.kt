package com.project.najdiprevoz.repositories

import com.project.najdiprevoz.domain.City
import com.project.najdiprevoz.domain.Ride
import com.project.najdiprevoz.repositories.projections.AvailableSeatsForRideProjection
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.sql.Driver

@Repository
interface RideRepository : JpaRepository<Ride, Long>, JpaSpecificationExecutor<Ride> {

    fun findAllByDriver(driver: Driver): List<Ride>?

    //    @Query("SELECT r from Ride r JOIN City c ON c.id = r.destination")
    fun findAllByDestination(destination: City): List<Ride>?

    @Query("""SELECT (r.totalSeats - count(rd.id)) as available_seats 
         FROM RideRequest rd 
         JOIN Ride r 
         ON r = rd.ride 
         WHERE rd.status = 'Approved'
         AND r = :ride
         GROUP BY r.id""")
    fun getAvailableSeatsForRide(@Param("ride") ride: Ride): AvailableSeatsForRideProjection

    fun findAllByFromLocation(fromLocation: City): List<Ride>?

    fun findById(id: Int): Ride?
}