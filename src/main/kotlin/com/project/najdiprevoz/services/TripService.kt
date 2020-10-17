package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.Ride
import com.project.najdiprevoz.enums.NotificationType
import com.project.najdiprevoz.enums.RideRequestStatus
import com.project.najdiprevoz.enums.RideStatus
import com.project.najdiprevoz.exceptions.RideNotFoundException
import com.project.najdiprevoz.repositories.*
import com.project.najdiprevoz.web.request.FilterTripRequest
import com.project.najdiprevoz.web.request.create.CreateTripRequest
import com.project.najdiprevoz.web.request.edit.EditTripRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Service
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.transaction.Transactional

//TODO: AVOID USING THIS MUCH SERVICES

@Service
class TripService(private val repository: RideRepository,
                  private val userService: UserService,
                  private val cityService: CityService,
                  private val notificationService: NotificationService) {

    val logger: Logger = LoggerFactory.getLogger(TripService::class.java)

    fun findById(id: Long): Ride =
            repository.findById(id)
                    .orElseThrow { RideNotFoundException("Ride with id $id was not found") }

    fun findAllTripsByDriverId(driverId: Long) =
            repository.findAllByDriverId(driverId = driverId)

    fun findAllFiltered(req: FilterTripRequest): List<Ride> = with(req) {
        val specification = if (departureDate != null)
            createRideSpecification(fromAddress = fromLocation, toAddress = toLocation,
                    departure = ZonedDateTime.ofInstant(departureDate.toInstant(), ZoneId.systemDefault())) //TODO: refactor this

        else createRideSpecification(fromAddress = fromLocation, toAddress = toLocation, departure = null)

        if (requestedSeats != null) {
            return repository.findAll(specification)
                    .filter { it.getAvailableSeats() >= requestedSeats }
        }
        return repository.findAll(specification)
    }

    fun findAllActiveTripsForToday(): List<Ride> =
            repository.findAll(evaluateSpecification(listOf("departureTime"), ZonedDateTime.now(), ::laterThanTime))


    fun getPastPublishedTripsByUser(userId: Long) =
            repository.findAllByDriverIdAndStatus(driverId = userId, status = RideStatus.FINISHED)

    fun findMyPastTripsAsPassenger(username: String) =
            repository.findMyPastTripsAsPassenger(username)


    fun createNewTrip(createTripRequest: CreateTripRequest, username: String) {
        logger.info("[RideService - ADD RIDE] Creating new ride!")
        repository.save(createRideObject(createTripRequest = createTripRequest, username = username))
    }

    @Modifying
    @Transactional
    fun editTrip(rideId: Long, editTripRequest: EditTripRequest): Ride = with(editTripRequest) {
        logger.info("[RideService - Edit Ride] Editing ride with ID:[$rideId]..")
        repository.save(findById(rideId).copy(
                fromLocation = cityService.findByName(fromLocation),
                departureTime = departureTime,
                destination = cityService.findByName(toLocation),
                additionalDescription = description,
                pricePerHead = pricePerHead))
    }

    @Transactional
    @Modifying
    fun cancelTrip(rideId: Long) {
        repository.changeRideStatus(rideId, RideStatus.CANCELLED)
        val ride = findById(rideId)
        ride.rideRequests = ride.rideRequests.map { it.changeStatus(RideRequestStatus.RIDE_CANCELLED) }
        ride.rideRequests.forEach {
            notificationService.pushRideRequestStatusChangeNotification(it, NotificationType.RIDE_CANCELLED)
        }
        logger.info("[RideService - CANCEL RIDE] Ride with id $rideId successfully cancelled!")
    }

    fun getMyTripsAsDriver(username: String): List<Ride> =
            repository.findAllByDriverUsername(username)

    fun getMyTripsAsPassenger(username: String): List<Ride> =
            repository.findAllMyTripsAsPassenger(username)


    fun canSubmitRating(ride: Ride, username: String): Boolean =
            repository.canSubmitRating(username, ride).isEmpty()

    private fun createRideSpecification(fromAddress: Long, toAddress: Long, departure: ZonedDateTime?) =
            listOfNotNull(
                    evaluateSpecification(listOf("fromLocation", "id"), fromAddress, ::equalSpecification),
                    evaluateSpecification(listOf("destination", "id"), toAddress, ::equalSpecification),
                    evaluateSpecification(listOf("departureTime"), departure, ::laterThanTime),
                    evaluateSpecification(listOf("status"), RideStatus.ACTIVE, ::tripStatusEqualsSpecification)
            ).fold(whereTrue()) { first, second ->
                Specification.where(first).and(second)
            }

    private fun createRideObject(createTripRequest: CreateTripRequest, username: String) = with(createTripRequest) {
        Ride(
                createdOn = ZonedDateTime.now(),
                fromLocation = cityService.findById(fromLocation),
                destination = cityService.findById(destination),
                departureTime = departureTime,
                totalSeatsOffered = totalSeats,
                driver = userService.findUserByUsername(username),
                pricePerHead = pricePerHead,
                additionalDescription = additionalDescription,
                rideRequests = listOf(),
                status = RideStatus.ACTIVE,
                isSmokingAllowed = smokingAllowed,
                isPetAllowed = petAllowed,
                hasAirCondition = hasAirCondition,
                maxTwoBackSeat = maxTwoBackseat)
    }

    private inline fun <reified T> evaluateSpecification(properties: List<String>, value: T?,
                                                         fn: (List<String>, T) -> Specification<Ride>) = value?.let {
        fn(properties, value)
    }

    fun checkForFinishedTripsCronJob() {
        logger.info("[CRONJOB] Checking for finished rides..")
        logger.info("[CRONJOB] Updated [" + repository.updateRidesCron(ZonedDateTime.now()) + "] rides.")
    }
}

