package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.ReservationRequest
import com.project.najdiprevoz.enums.ReservationStatus
import com.project.najdiprevoz.enums.TripStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.repository.Modifying
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class CronJobService(private val reservationRequestService: ReservationRequestService,
                     private val tripService: TripService,
                     private val ratingService: RatingService) {

    val logger: Logger = LoggerFactory.getLogger(CronJobService::class.java)

    @Scheduled(fixedRate = 1000000)
    @Modifying
    @Transactional
    fun updateRidesAndRequestsJob() {
        updateRideCron()
        logger.info("[CRONJOB] Updating EXPIRED and RIDE_CANCELLED ride requests..")
        updateReservationRequestCron()
    }

    private fun updateReservationRequestCron() {
        val allReservationRequests = reservationRequestService.findAll()

        allReservationRequests
                .filter { it.status == ReservationStatus.PENDING && it.trip.status == TripStatus.FINISHED }
                .forEach { changeRequestToExpired(it) }

        allReservationRequests
                .filter {
                    it.status == ReservationStatus.APPROVED && it.trip.status == TripStatus.FINISHED && !checkIfHasRatingAllowedNotification(it)
                }
                .forEach { sendRatingNotification(it) }
    }

    private fun sendRatingNotification(it: ReservationRequest) =
            ratingService.pushRatingAllowedNotification(it)

    private fun changeRequestToExpired(reservationRequest: ReservationRequest) =
            reservationRequestService.reservationRequestCronJob(reservationRequest)

    private fun updateRideCron() =
            tripService.checkForFinishedTripsCronJob()

    private fun checkIfHasRatingAllowedNotification(reservationRequest: ReservationRequest): Boolean =
            ratingService.checkIfHasRatingAllowedNotification(reservationRequest)
}