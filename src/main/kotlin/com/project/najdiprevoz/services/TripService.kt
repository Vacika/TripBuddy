package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.Trip
import com.project.najdiprevoz.enums.NotificationType
import com.project.najdiprevoz.enums.ReservationStatus
import com.project.najdiprevoz.enums.TripStatus
import com.project.najdiprevoz.exceptions.RideNotFoundException
import com.project.najdiprevoz.exceptions.SeatsLimitException
import com.project.najdiprevoz.repositories.RideRepository
import com.project.najdiprevoz.utils.*
import com.project.najdiprevoz.web.request.FilterTripRequest
import com.project.najdiprevoz.web.request.create.CreateTripRequest
import com.project.najdiprevoz.web.request.edit.EditTripRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
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
                  private val notificationService: NotificationService,
                  @Value("\${najdiprevoz.max-seats-per-trip}")
                  private val maxSeatsPerTrip: Int) {

    val logger: Logger = LoggerFactory.getLogger(TripService::class.java)

    fun findById(id: Long): Trip =
            repository.findById(id)
                    .orElseThrow { RideNotFoundException("Ride with id $id was not found") }

    fun findAllTripsByDriverId(driverId: Long) =
            repository.findAllByDriverId(driverId = driverId)

    fun findAllFiltered(req: FilterTripRequest): List<Trip> = with(req) {
        val specification = if (departureDate != null)
            createRideSpecification(fromAddress = fromLocation, toAddress = toLocation,
                    departure = ZonedDateTime.ofInstant(departureDate.toInstant(), ZoneId.systemDefault()), availableSeats = requestedSeats) //TODO: refactor this

        else createRideSpecification(fromAddress = fromLocation, toAddress = toLocation, departure = null, availableSeats = requestedSeats)
        return repository.findAll(specification)
    }

    fun findAllActiveTripsForToday(): List<Trip> =
            repository.findAll(
                    listOfNotNull(
                            evaluateSpecification(listOf("departureTime"), ZonedDateTime.now(), ::laterThanTime),
                            evaluateSpecification(listOf("status"), TripStatus.ACTIVE, ::tripStatusEqualsSpecification))
                            .fold(whereTrue<Trip>()) { first, second -> Specification.where(first).and(second) })


    fun createNewTrip(createTripRequest: CreateTripRequest, username: String) {
        logger.info("[RideService - ADD RIDE] Creating new ride by {}!", username)
        if (createTripRequest.totalSeats > maxSeatsPerTrip){
            throw SeatsLimitException()
        }

        repository.save(createRideObject(createTripRequest = createTripRequest, username = username))
    }

    @Modifying
    @Transactional
    fun editTrip(rideId: Long, editTripRequest: EditTripRequest): Trip = with(editTripRequest) {
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
        repository.changeTripStatus(rideId, TripStatus.CANCELLED)
        val ride = findById(rideId)
        ride.reservationRequests = ride.reservationRequests.map { it.changeStatus(ReservationStatus.RIDE_CANCELLED) }
        ride.reservationRequests.forEach {
            notificationService.pushReservationRequestStatusChangeNotification(it, NotificationType.RIDE_CANCELLED)
        }
        logger.info("[RideService - CANCEL RIDE] Ride with id $rideId successfully cancelled!")
    }

    fun getMyTripsAsDriver(username: String): List<Trip> =
            repository.findAllByDriverUsername(username)

    fun getMyTripsAsPassenger(username: String): List<Trip> =
            repository.findAllMyTripsAsPassenger(username)


    private fun createRideSpecification(fromAddress: Long, toAddress: Long, departure: ZonedDateTime?, availableSeats: Int?) =
            listOfNotNull(
                    evaluateSpecification(listOf("fromLocation", "id"), fromAddress, ::equalSpecification),
                    evaluateSpecification(listOf("destination", "id"), toAddress, ::equalSpecification),
                    evaluateSpecification(listOf("availableSeats"), availableSeats, ::greaterThanOrEquals),
                    evaluateSpecification(listOf("departureTime"), departure, ::laterThanTime),
                    evaluateSpecification(listOf("status"), TripStatus.ACTIVE, ::tripStatusEqualsSpecification)
            ).fold(whereTrue<Trip>()) { first, second ->
                Specification.where(first).and(second)
            }

    private fun createRideObject(createTripRequest: CreateTripRequest, username: String) = with(createTripRequest) {
        Trip(
                createdOn = ZonedDateTime.now(),
                fromLocation = cityService.findById(fromLocation),
                destination = cityService.findById(destination),
                departureTime = departureTime,
                totalSeatsOffered = totalSeats,
                driver = userService.findUserByUsername(username),
                pricePerHead = pricePerHead,
                additionalDescription = additionalDescription,
                reservationRequests = listOf(),
                status = TripStatus.ACTIVE,
                isSmokingAllowed = smokingAllowed,
                isPetAllowed = petAllowed,
                hasAirCondition = hasAirCondition,
                maxTwoBackSeat = maxTwoBackseat,
                availableSeats = totalSeats)
    }

    private inline fun <reified T> evaluateSpecification(properties: List<String>, value: T?,
                                                         fn: (List<String>, T) -> Specification<Trip>) = value?.let {
        fn(properties, value)
    }

    fun checkForFinishedTripsCronJob() {
        logger.info("[CRONJOB] Checking for finished rides..")
        logger.info("[CRONJOB] Updated [" + repository.updateFinishedTripsCron(ZonedDateTime.now()) + "] rides.")
    }

    fun updateRideAvailableSeats(rideId: Long, seats: Int) {
        repository.updateTripAvailableSeats(rideId, seats)
    }

//    fun getMyTripsAsDriverPaginated(username: String, pageable: Pageable): Page<Ride> {
//        val spec = Specification.where(likeSpecification<Ride>(listOf("driver","username"), username))
//        return repository.findAll(spec, pageable)
//    }
}

