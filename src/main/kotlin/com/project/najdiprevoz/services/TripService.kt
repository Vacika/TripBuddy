package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.Trip
import com.project.najdiprevoz.enums.NotificationType
import com.project.najdiprevoz.enums.ReservationStatus
import com.project.najdiprevoz.enums.TripStatus
import com.project.najdiprevoz.exceptions.MinimumHrsBeforeCancelException
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
                  @Value("\${najdiprevoz.min-hrs-before-cancel-trip")
                  private val minHrsBeforeCancelTrip: Long,
                  @Value("\${najdiprevoz.max-seats-per-trip}")
                  private val maxSeatsPerTrip: Int) {

    val logger: Logger = LoggerFactory.getLogger(TripService::class.java)

    fun findById(id: Long): Trip =
            repository.findById(id)
                    .orElseThrow { RideNotFoundException("Trip with id $id was not found") }

    fun findAllTripsByDriverId(driverId: Long) =
            repository.findAllByDriverId(driverId = driverId)

    fun findAllFiltered(req: FilterTripRequest): List<Trip> = with(req) {
        val specification = if (departureDate != null)
            createTripSpecification(fromAddress = fromLocation, toAddress = toLocation,
                    departure = ZonedDateTime.ofInstant(departureDate.toInstant(), ZoneId.systemDefault()), availableSeats = requestedSeats) //TODO: refactor this

        else createTripSpecification(fromAddress = fromLocation, toAddress = toLocation, departure = null, availableSeats = requestedSeats)
        return repository.findAll(specification)
    }

    fun findAllActiveTripsForToday(): List<Trip> =
            repository.findAll(
                    listOfNotNull(
                            evaluateSpecification(listOf("departureTime"), ZonedDateTime.now(), ::laterThanTime),
                            evaluateSpecification(listOf("status"), TripStatus.ACTIVE, ::tripStatusEqualsSpecification))
                            .fold(whereTrue<Trip>()) { first, second -> Specification.where(first).and(second) })


    fun createNewTrip(createTripRequest: CreateTripRequest, username: String) {
        logger.info("[TripService - ADD TRIP] Creating new trip by {}!", username)
        if (createTripRequest.totalSeats > maxSeatsPerTrip){
            throw SeatsLimitException()
        }

        repository.save(createTripObject(createTripRequest = createTripRequest, username = username))
    }

    @Modifying
    @Transactional
    fun editTrip(tripId: Long, editTripRequest: EditTripRequest): Trip = with(editTripRequest) {
        logger.info("[TripService - Edit Trip] Editing trip with ID:[$tripId]..")
        repository.save(findById(tripId).copy(
                fromLocation = cityService.findByName(fromLocation),
                departureTime = departureTime,
                destination = cityService.findByName(toLocation),
                additionalDescription = description,
                pricePerHead = pricePerHead))
    }

    @Transactional
    @Modifying
    fun cancelTrip(tripId: Long, username: String) {
        checkIfCanCancelTrip(tripId, username)
        repository.changeTripStatus(tripId, TripStatus.CANCELLED)
        val trip = findById(tripId)
        trip.reservationRequests = trip.reservationRequests.map { it.changeStatus(ReservationStatus.RIDE_CANCELLED) }
        trip.reservationRequests.forEach {
            notificationService.pushReservationRequestStatusChangeNotification(it, NotificationType.RIDE_CANCELLED)
        }
        logger.info("[TripService - CANCEL trip] Trip with id $tripId successfully cancelled!")
    }

    /***
     * 1) Check if the user who sent the API request is the same as the driver
     * 2) Checks if the trip's departure is minHrs after the time now, when the cancel_trip action was taken.
     * If not, Cancel Trip action is forbidden and the trip wont be cancelled.
     ***/
    private fun checkIfCanCancelTrip(tripId: Long, username: String) {
        val trip = findById(tripId)
        if(trip.driver.username != username){
            throw Exception()
        }
        if(trip.departureTime.minusHours(minHrsBeforeCancelTrip).isBefore(ZonedDateTime.now())){
            logger.error("The trip with ID: [$tripId] can not be cancelled, the cancellation time has passed..")
            throw MinimumHrsBeforeCancelException()
        }
    }

    fun getMyTripsAsDriver(username: String): List<Trip> =
            repository.findAllByDriverUsername(username)

    fun getMyTripsAsPassenger(username: String): List<Trip> =
            repository.findAllMyTripsAsPassenger(username)


    private fun createTripSpecification(fromAddress: Long, toAddress: Long, departure: ZonedDateTime?, availableSeats: Int?) =
            listOfNotNull(
                    evaluateSpecification(listOf("fromLocation", "id"), fromAddress, ::equalSpecification),
                    evaluateSpecification(listOf("destination", "id"), toAddress, ::equalSpecification),
                    evaluateSpecification(listOf("availableSeats"), availableSeats, ::greaterThanOrEquals),
                    evaluateSpecification(listOf("departureTime"), departure, ::laterThanTime),
                    evaluateSpecification(listOf("status"), TripStatus.ACTIVE, ::tripStatusEqualsSpecification)
            ).fold(whereTrue<Trip>()) { first, second ->
                Specification.where(first).and(second)
            }

    private fun createTripObject(createTripRequest: CreateTripRequest, username: String) = with(createTripRequest) {
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
        logger.info("[CRONJOB] Checking for finished trips..")
        logger.info("[CRONJOB] Updated [" + repository.updateFinishedTripsCron(ZonedDateTime.now()) + "] trips.")
    }

    fun updateTripAvailableSeats(tripId: Long, seats: Int) {
        repository.updateTripAvailableSeats(tripId, seats)
    }

//    fun getMyTripsAsDriverPaginated(username: String, pageable: Pageable): Page<Trip> {
//        val spec = Specification.where(likeSpecification<Trip>(listOf("driver","username"), username))
//        return repository.findAll(spec, pageable)
//    }
}

