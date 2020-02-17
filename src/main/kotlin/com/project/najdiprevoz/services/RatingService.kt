package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.Rating
import com.project.najdiprevoz.repositories.RatingRepository
import com.project.najdiprevoz.web.request.create.CreateRatingRequest
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class RatingService(private val repository: RatingRepository,
                    private val memberService: MemberService,
                    private val rideRequestService: RideRequestService,
                    private val notificationService: NotificationService) {

//    fun getRatingsForRide(rideId: Long) =
//            repository.findRatingsByRideId(rideId = rideId)

    fun getRatingsForMember(memberId: Long) =
            repository.findRatingsForDriverId(driverId = memberId)

//    fun getRatingsSubmittedByMember(memberId: Long) =
//            repository.findRatingsByAuthorId(authorId = memberId)

    fun addRating(createRatingRequest: CreateRatingRequest) = with(createRatingRequest) {
        notificationService.pushRatingNotification(repository.save(Rating(
                rideRequest = rideRequestService.findById(rideRequestId),
                note = note,
                dateSubmitted = ZonedDateTime.now(),
                rating = rating
        )))
    }
}