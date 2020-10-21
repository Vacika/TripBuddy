package com.project.najdiprevoz.mapper

import com.project.najdiprevoz.domain.Ride
import com.project.najdiprevoz.domain.User
import com.project.najdiprevoz.enums.RideStatus
import com.project.najdiprevoz.repositories.RatingViewRepository
import com.project.najdiprevoz.services.TripService
import com.project.najdiprevoz.utils.PageableUtils
import com.project.najdiprevoz.web.request.FilterTripRequest
import com.project.najdiprevoz.web.request.create.CreateTripRequest
import com.project.najdiprevoz.web.request.edit.EditTripRequest
import com.project.najdiprevoz.web.response.PastTripResponse
import com.project.najdiprevoz.web.response.TripDetailsResponse
import com.project.najdiprevoz.web.response.TripResponse
import com.project.najdiprevoz.web.response.UserShortResponse
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import java.awt.print.Pageable

@Service
class TripMapper(private val tripService: TripService,
                 private val ratingViewRepository: RatingViewRepository) {

    //////////////////CORE METHODS////////////////////
    fun findAllActiveTripsForToday(): List<TripResponse> {
        return tripService.findAllActiveTripsForToday().map { mapToTripResponse(it) }
    }

    fun findAllFiltered(req: FilterTripRequest): List<TripResponse> =
            tripService.findAllFiltered(req)
                    .map(::mapToTripResponse)
                    .sortedByDescending { it.driver.rating }

    fun editTrip(rideId: Long, editTripRequest: EditTripRequest): TripResponse =
            mapToTripResponse(tripService.editTrip(rideId, editTripRequest))

    fun createNewTrip(createTripRequest: CreateTripRequest, username: String) =
            tripService.createNewTrip(createTripRequest, username)

    fun cancelTrip(rideId: Long) =
            tripService.cancelTrip(rideId)

    fun findById(rideId: Long): TripResponse =
            mapToTripResponse(tripService.findById(rideId))

    fun getTripAdditionalInfo(tripId: Long): TripDetailsResponse {
        return mapToTripDetailsResponse(tripService.findById(tripId))
    }

    ///////////////////////////////////////////////////



    fun getPastPublishedTripsByUser(userId: Long): List<TripResponse> =
            tripService.getPastPublishedTripsByUser(userId).map(::mapToTripResponse)

    fun findMyPastTripsAsPassenger(username: String) =
            tripService.findMyPastTripsAsPassenger(username).map { mapToPastTripResponse(it, username) }

    fun getMyTripsAsDriver(username: String): List<TripResponse> {
        return tripService.getMyTripsAsDriver(username).map(::mapToTripResponse)
    }

    fun getMyTripsAsPassenger(username: String): List<TripResponse> =
            tripService.getMyTripsAsPassenger(username).map(::mapToTripResponse)
    fun findAllTripsByDriverId(id: Long) =
            tripService.findAllTripsByDriverId(id).map(::mapToTripResponse)


    private fun mapToTripResponse(trip: Ride): TripResponse = with(trip) {
        return TripResponse(id = id,
                from = fromLocation.name,
                to = destination.name,
                departureTime = departureTime,
                availableSeats = availableSeats,
                pricePerHead = pricePerHead,
                totalSeats = totalSeatsOffered,
                driver = mapToUserShortResponse(driver),
                maxTwoBackSeat = maxTwoBackSeat,
                status = status.name,
                allowedActions = if (status == RideStatus.ACTIVE) listOf("CANCEL_RIDE") else emptyList())
    }

    private fun mapToPastTripResponse(ride: Ride, username: String):PastTripResponse = with(ride) {
        PastTripResponse(
                tripId = id,
                from = fromLocation.name,
                to = destination.name,
                pricePerHead = pricePerHead,
                driver = mapToUserShortResponse(driver),
                canSubmitRating = tripService.canSubmitRating(ride, username)
        )
    }



    private fun mapToTripDetailsResponse(trip: Ride): TripDetailsResponse = with(trip) {
        return TripDetailsResponse(isPetAllowed = isPetAllowed,
                isSmokingAllowed = isSmokingAllowed,
                hasAirCondition = hasAirCondition,
                additionalDescription = trip.additionalDescription)
    }

    private fun mapToUserShortResponse(user: User): UserShortResponse = with(user){
        UserShortResponse(id = id,
                rating = ratingViewRepository.findAllByDriverId(id)
                        .map { it.rating }
                        .average(),
                name = this.getFullName(),
                profilePhoto = this.profilePhoto)
    }


//    fun getMyTripsAsDriver(username: String, page: Int, pageSize: Int): Page<TripResponse> {
//        val pageable = PageableUtils.getPageableWithDefaultSortById(page,pageSize,null,null)
//        return tripService.getMyTripsAsDriverPaginated(username, pageable).let {
//            PageableUtils.createPageResponse(
//                    it,
//                    it.content.map(::mapToTripResponse),
//                    it.pageable
//            )
//        }
//    }
}