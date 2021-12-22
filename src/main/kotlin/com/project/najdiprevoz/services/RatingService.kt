package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.Rating
import com.project.najdiprevoz.domain.ReservationRequest
import com.project.najdiprevoz.enums.ReservationStatus
import com.project.najdiprevoz.enums.TripStatus
import com.project.najdiprevoz.exceptions.AddRatingFailedException
import com.project.najdiprevoz.repositories.RatingRepository
import com.project.najdiprevoz.web.request.create.CreateRatingRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class RatingService(
    private val repository: RatingRepository,
    private val reservationRequestService: ReservationRequestService,
    private val notificationService: NotificationService
) {

    val logger: Logger = LoggerFactory.getLogger(RatingService::class.java)

    fun addRating(createRatingRequest: CreateRatingRequest) = with(createRatingRequest) {
        when (canAddRating(this)) {
            true -> pushRatingNotification(this)
            false -> throw AddRatingFailedException("The request is not APPROVED or member has already submitted rating for this ride")
        }
    }

    fun pushRatingAllowedNotification(reservationRequest: ReservationRequest) {
        logger.debug("[RatingService] Pushing Rating Allowed Notification for ReservationRequest with ID: [${reservationRequest.id}")
        notificationService.pushRatingAllowedNotification(reservationRequest = reservationRequest)
    }

    fun checkIfHasRatingAllowedNotification(reservationRequest: ReservationRequest) =
        notificationService.checkIfHasRatingAllowedNotification(reservationRequest)

    // Return true if the reservation has been approved and the member has not submitted rating for this ride previously!
    private fun canAddRating(createRatingRequest: CreateRatingRequest) = with(createRatingRequest) {
        logger.debug("[RatingService] Checking if there is already submitted rating for ReservationRequest with ID: $reservationRequestId")
        val reservationRequest = reservationRequestService.findById(reservationRequestId)
        reservationRequest.status == ReservationStatus.APPROVED && reservationRequest.rating == null && reservationRequest.trip.status == TripStatus.FINISHED
    }

    private fun pushRatingNotification(createRatingRequest: CreateRatingRequest) = with(createRatingRequest) {
        logger.debug("[RatingService] Adding new rating for ReservationRequest with ID: [${reservationRequestId}")
        notificationService.removeOldNotificationsActions(reservationRequestId)
//        notificationService.removeLastNotificationForReservation(reservationRequestId, reservation.requester) //TODO: Check this logic
        notificationService.pushRatingSubmittedNotification(
            repository.save(
                Rating(
                    reservationRequest = reservationRequestService.findById(reservationRequestId),
                    note = note,
                    dateSubmitted = ZonedDateTime.now(),
                    rating = rating
                )
            )
        )
    }
}