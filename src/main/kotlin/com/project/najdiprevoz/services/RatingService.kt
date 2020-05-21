package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.Rating
import com.project.najdiprevoz.domain.RideRequest
import com.project.najdiprevoz.enums.RideRequestStatus
import com.project.najdiprevoz.enums.RideStatus
import com.project.najdiprevoz.exceptions.AddRatingFailedException
import com.project.najdiprevoz.repositories.RatingRepository
import com.project.najdiprevoz.web.request.create.CreateRatingRequest
import com.project.najdiprevoz.web.response.RatingResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class RatingService(private val repository: RatingRepository,
                    private val rideRequestService: RideRequestService,
                    private val notificationService: NotificationService) {

    val logger: Logger = LoggerFactory.getLogger(RatingService::class.java)

    fun getRatingsForTrip(rideId: Long) =
            repository.findRatingsByRideRequestRide_Id(rideId = rideId)

    fun getRatingsForUser(username: String) =
            repository.findAllByRatedUser_Username(username).map(::mapToRatingResponse)

    fun addRating(createRatingRequest: CreateRatingRequest) = with(createRatingRequest) {
        when (canAddRating(this)) {
            true -> pushRatingNotification(this)
            false -> throw AddRatingFailedException("The request is not APPROVED or member has already submitted rating for this ride")
        }
    }

    private fun pushRatingNotification(createRatingRequest: CreateRatingRequest) = with(createRatingRequest) {
        logger.debug("[RatingService] Adding new rating for RideRequest with ID: [${rideRequestId}")
        notificationService.removeLastNotificationForRideRequest(rideRequestId)
        notificationService.pushRatingNotification(
                repository.save(Rating(
                        rideRequest = rideRequestService.findById(rideRequestId),
                        note = note,
                        dateSubmitted = ZonedDateTime.now(),
                        rating = rating
                )))
    }

    fun pushRatingAllowedNotification(rideRequest: RideRequest){
        logger.debug("[RatingService] Pushing RatingNotification for RideRequest with ID: [${rideRequest.id}")
        notificationService.pushRatingAllowedNotification(rideRequest = rideRequest)
    }

    fun checkIfHasRatingAllowedNotification(rideRequest: RideRequest) =
        notificationService.checkIfHasRatingAllowedNotification(rideRequest)

    // Return true if the request has been approved and the member has not submitted rating for this ride previously!
    private fun canAddRating(createRatingRequest: CreateRatingRequest) = with(createRatingRequest) {
        logger.debug("[RatingService] Checking if there is already submitted rating for RideRequest with ID: $rideRequestId")
        val rideRequest = rideRequestService.findById(rideRequestId)
        rideRequest.status == RideRequestStatus.APPROVED && rideRequest.rating == null && rideRequest.ride.status == RideStatus.FINISHED
    }

    private fun mapToRatingResponse(rating: Rating): RatingResponse {
        return RatingResponse(
                id = rating.id,
                rating = rating.rating,
                note = rating.note,
                rideId = rating.rideRequest.ride.id,
                fromFullName = rating.getAuthor().getFullName(),
                fromId = rating.getAuthor().id,
                fromProfilePic = rating.getAuthor().profilePhoto,
                rideDate = rating.rideRequest.ride.departureTime,
                rideFrom = rating.rideRequest.ride.fromLocation.name,
                rideTo = rating.rideRequest.ride.destination.name
        )
    }
}