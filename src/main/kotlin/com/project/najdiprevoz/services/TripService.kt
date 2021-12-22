package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.Trip
import com.project.najdiprevoz.enums.NotificationType
import com.project.najdiprevoz.enums.ReservationStatus
import com.project.najdiprevoz.enums.TripStatus
import com.project.najdiprevoz.exceptions.MinimumHrsBeforeCancelException
import com.project.najdiprevoz.exceptions.RideNotFoundException
import com.project.najdiprevoz.exceptions.SeatsLimitException
import com.project.najdiprevoz.repositories.RideRepository
import com.project.najdiprevoz.services.sms.SmsTripNotificationService
import com.project.najdiprevoz.web.request.create.CreateTripRequest
import com.project.najdiprevoz.web.request.edit.EditTripRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import javax.transaction.Transactional

//TODO: AVOID USING THIS MUCH SERVICES

@Service
class TripService(
    private val repository: RideRepository,
    private val userService: UserService,
    private val cityService: CityService,
    private val notificationService: NotificationService,
    @Value("\${najdiprevoz.min-hrs-before-cancel-trip}")
    private val minHrsBeforeCancelTrip: Long,
    @Value("\${najdiprevoz.max-seats-per-trip}")
    private val maxSeatsPerTrip: Int,
    private val smsTripNotificationService: SmsTripNotificationService,
    private val reservationRequestService: ReservationRequestService
) {

    val logger: Logger = LoggerFactory.getLogger(TripService::class.java)

    fun findById(id: Long): Trip =
        repository.findById(id)
            .orElseThrow { RideNotFoundException("Trip with id $id was not found") }

    @Transactional
    fun create(createTripRequest: CreateTripRequest, username: String) {
        logger.info("[TripService - ADD TRIP] Creating new trip by {}!", username)
        checkEnoughSeats(createTripRequest)
        createTripObject(createTripRequest = createTripRequest, username = username).let {
            smsTripNotificationService.checkForSmsNotifications(it)
            repository.save(it)
        }
    }

    @Transactional
    @Modifying
    fun edit(tripId: Long, editTripRequest: EditTripRequest): Trip = with(editTripRequest) {
        logger.info("[TripService - Edit Trip] Editing trip with ID:[$tripId]..")
        repository.save(
            findById(tripId).copy(
                fromLocation = cityService.findByName(fromLocation),
                departureTime = departureTime,
                destination = cityService.findByName(toLocation),
                additionalDescription = description,
                pricePerHead = pricePerHead
            )
        )
    }

    fun updateAvailableSeats(tripId: Long, seats: Int) {
        repository.updateTripAvailableSeats(tripId, seats)
    }

    @Transactional
    fun cancelTrip(tripId: Long, username: String) {
        checkCanCancelTrip(tripId, username)
        findById(tripId).let { trip ->
            trip.reservationRequests.forEach { resRequest ->
                reservationRequestService.changeStatus(resRequest.id, ReservationStatus.RIDE_CANCELLED)
                notificationService.pushReservationStatusChangeNotification(
                    resRequest,
                    NotificationType.RIDE_CANCELLED
                )
            }
            repository.changeTripStatus(tripId, TripStatus.CANCELLED)
            logger.info("[TripService - CANCEL Trip] Trip with id $tripId successfully cancelled!")
        }
    }

    /***
     * 1) Check if the user who sent the API request is the same as the driver
     * 2) Checks if the trip's departure is minHrs after the time now, when the cancel_trip action was taken.
     * If not, Cancel Trip action is forbidden and the trip wont be cancelled.
     ***/
    private fun checkCanCancelTrip(tripId: Long, username: String) {
        val trip = findById(tripId)
        if (trip.driver.username != username) {
            throw Exception("User who sent the request is not matching with the driver")
        }
        if (trip.departureTime.minusHours(minHrsBeforeCancelTrip).isBefore(ZonedDateTime.now())) {
            logger.error("The trip with ID: [$tripId] can not be cancelled, the cancellation time has passed..")
            throw MinimumHrsBeforeCancelException()
        }
    }

    private fun checkEnoughSeats(createTripRequest: CreateTripRequest) {
        if (createTripRequest.totalSeats > maxSeatsPerTrip) {
            throw SeatsLimitException()
        }
    }

    private fun createTripObject(createTripRequest: CreateTripRequest, username: String) = with(createTripRequest) {
        Trip(
            createdOn = ZonedDateTime.now(),
            fromLocation = cityService.findById(fromLocation),
            destination = cityService.findById(destination),
            departureTime = departureTime,
            totalSeatsOffered = totalSeats,
            driver = userService.findByUsername(username),
            pricePerHead = pricePerHead,
            additionalDescription = additionalDescription,
            reservationRequests = listOf(),
            status = TripStatus.ACTIVE,
            isSmokingAllowed = smokingAllowed,
            isPetAllowed = petAllowed,
            hasAirCondition = hasAirCondition,
            maxTwoBackSeat = maxTwoBackseat,
            availableSeats = totalSeats
        )
    }

    @Transactional
    fun updateFinishedTripsCronJob() {
        logger.info("[CRONJOB] Checking for finished trips..")
        logger.info("[CRONJOB] Updated [" + repository.updateFinishedTripsCron(ZonedDateTime.now()) + "] trips.")
    }
}

