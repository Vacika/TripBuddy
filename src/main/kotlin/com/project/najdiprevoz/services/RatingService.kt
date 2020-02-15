package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.Rating
import com.project.najdiprevoz.repositories.RatingRepository
import com.project.najdiprevoz.web.request.create.CreateRatingRequest
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import javax.annotation.PostConstruct
import javax.transaction.Transactional

@Service
class RatingService(private val repository: RatingRepository,
                    private val memberService: MemberService,
                    private val rideService: RideService) {

    fun getRatingsForRide(rideId: Long) =
            repository.findRatingsByRide_Id(rideId = rideId)

    fun getRatingsForMember(memberId: Long) =
            repository.findRatingsForDriver_Id(driverId = memberId)

    fun getRatingsSubmittedByMember(memberId: Long) =
            repository.findRatingsByAuthor_Id(authorId = memberId)

    fun addRating(createRatingRequest: CreateRatingRequest) = with(createRatingRequest) {
        repository.save(Rating(
                author = memberService.findMemberById(memberId = authorId),
                ride = rideService.findById(rideId),
                note = note,
                dateSubmitted = ZonedDateTime.now(),
                rating = rating
        ))
    }
////
//    @PostConstruct
//    fun test(){
////        addRating(CreateRatingRequest(
////                rating=2,
////                rideId = 1,
////                note="Kolata nemase klima",
////                authorId = 1
////        ))
////        val t = getRatingsForMember(1)
//    }
}