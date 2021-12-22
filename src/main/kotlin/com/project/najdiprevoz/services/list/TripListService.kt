package com.project.najdiprevoz.services.list

import com.project.najdiprevoz.domain.Trip
import com.project.najdiprevoz.enums.TripStatus
import com.project.najdiprevoz.repositories.RideRepository
import com.project.najdiprevoz.utils.*
import com.project.najdiprevoz.web.request.FilterTripRequest
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.time.ZoneId
import java.time.ZonedDateTime

@Service
class TripListService(private val repository: RideRepository){
    fun findAllActiveForToday(): List<Trip> =
        repository.findAll(
            listOfNotNull(
                evaluateSpecification(listOf("departureTime"), ZonedDateTime.now(), ::laterThanTime),
                evaluateSpecification(listOf("status"), TripStatus.ACTIVE, ::tripStatusEqualsSpecification)
            )
                .fold(whereTrue<Trip>()) { first, second -> Specification.where(first).and(second) })

    fun findAllFiltered(req: FilterTripRequest): List<Trip> = with(req) {
        val specification = if (departureDate != null)
            getTripSpecification(
                fromAddress = fromLocation,
                toAddress = toLocation,
                departure = ZonedDateTime.ofInstant(departureDate.toInstant(), ZoneId.systemDefault()),
                availableSeats = requestedSeats
            ) //TODO: refactor this

        else getTripSpecification(
            fromAddress = fromLocation,
            toAddress = toLocation,
            departure = null,
            availableSeats = requestedSeats
        )
        return repository.findAll(specification)
    }

    fun findAllByDriverId(driverId: Long) =
        repository.findAllByDriverId(driverId = driverId)

    fun getMyTripsAsDriver(username: String): List<Trip> =
        repository.findAllByDriverUsername(username)

    fun getMyTripsAsPassenger(username: String): List<Trip> =
        repository.findAllMyTripsAsPassenger(username)


    private fun getTripSpecification(
        fromAddress: Long,
        toAddress: Long,
        departure: ZonedDateTime?,
        availableSeats: Int?
    ) =
        listOfNotNull(
            evaluateSpecification(listOf("fromLocation", "id"), fromAddress, ::equalSpecification),
            evaluateSpecification(listOf("destination", "id"), toAddress, ::equalSpecification),
            evaluateSpecification(listOf("availableSeats"), availableSeats, ::greaterThanOrEquals),
            evaluateSpecification(listOf("departureTime"), departure, ::laterThanTime),
            evaluateSpecification(listOf("status"), TripStatus.ACTIVE, ::tripStatusEqualsSpecification)
        ).fold(whereTrue<Trip>()) { first, second ->
            Specification.where(first).and(second)
        }

    private inline fun <reified T> evaluateSpecification(
        properties: List<String>, value: T?,
        fn: (List<String>, T) -> Specification<Trip>
    ) = value?.let {
        fn(properties, value)
    }
}