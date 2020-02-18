package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.Rating
import com.project.najdiprevoz.enums.RequestStatus
import com.project.najdiprevoz.exceptions.AddRatingFailedException
import com.project.najdiprevoz.repositories.RatingRepository
import com.project.najdiprevoz.web.request.create.CreateRatingRequest
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class RatingService(private val repository: RatingRepository,
                    private val rideRequestService: RideRequestService,
                    private val notificationService: NotificationService) {

    fun getRatingsForRide(rideId: Long) =
            repository.findRatingsByRideRequestRide_Id(rideId = rideId)

    fun getRatingsForMember(userId: Long) =
            repository.findRatingsForDriverId(driverId = userId)

    fun addRating(createRatingRequest: CreateRatingRequest) = with(createRatingRequest) {
        when (canAddRating(this)) {
            true -> pushRatingNotification(this)
            false -> throw AddRatingFailedException("The request is not APPROVED or member has already submitted rating for this ride")
        }
    }

    private fun pushRatingNotification(createRatingRequest: CreateRatingRequest) = with(createRatingRequest) {
        notificationService.pushRatingNotification(
                repository.save(Rating(
                        rideRequest = rideRequestService.findById(rideRequestId),
                        note = note,
                        dateSubmitted = ZonedDateTime.now(),
                        rating = rating
                )))
    }

    // Return true if the request has been approved and the member has not submitted rating for this ride previously!
    private fun canAddRating(createRatingRequest: CreateRatingRequest) = with(createRatingRequest) {
        val rideRequest = rideRequestService.findById(rideRequestId)
        rideRequest.status == RequestStatus.APPROVED && rideRequest.rating == null  // TODO: AVOID checking rideRequest.rating, instead DATABASE SHOULD FORBID DUPLICATE FOREIGN KEYS!
    }
}