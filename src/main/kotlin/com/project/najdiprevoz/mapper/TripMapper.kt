package com.project.najdiprevoz.mapper

import com.project.najdiprevoz.domain.Trip
import com.project.najdiprevoz.domain.User
import com.project.najdiprevoz.enums.Actions
import com.project.najdiprevoz.enums.TripStatus
import com.project.najdiprevoz.repositories.RatingViewRepository
import com.project.najdiprevoz.services.TripService
import com.project.najdiprevoz.web.request.FilterTripRequest
import com.project.najdiprevoz.web.request.create.CreateTripRequest
import com.project.najdiprevoz.web.request.edit.EditTripRequest
import com.project.najdiprevoz.web.response.TripDetailsResponse
import com.project.najdiprevoz.web.response.TripResponse
import com.project.najdiprevoz.web.response.UserShortResponse
import org.springframework.stereotype.Service

@Service
class TripMapper(private val tripService: TripService,
                 private val ratingViewRepository: RatingViewRepository) {

    //////////////////CORE METHODS////////////////////
    fun findAllActiveTripsForToday(): List<TripResponse> {
        return tripService.findAllActiveTripsForToday().map { mapToTripResponse(it, false) }
    }

    fun findAllFiltered(req: FilterTripRequest): List<TripResponse> =
            tripService.findAllFiltered(req)
                    .map { mapToTripResponse(it, false) }
                    .sortedByDescending { it.driver.rating }

    fun editTrip(rideId: Long, editTripRequest: EditTripRequest): TripResponse =
            mapToTripResponse(tripService.editTrip(rideId, editTripRequest), false)

    fun createNewTrip(createTripRequest: CreateTripRequest, username: String) =
            tripService.createNewTrip(createTripRequest, username)

    fun cancelTrip(rideId: Long) =
            tripService.cancelTrip(rideId)

    fun findById(rideId: Long): TripResponse =
            mapToTripResponse(tripService.findById(rideId), false)

    fun getTripAdditionalInfo(tripId: Long): TripDetailsResponse {
        return mapToTripDetailsResponse(tripService.findById(tripId))
    }

    ///////////////////////////////////////////////////

    fun getMyTripsAsDriver(username: String): List<TripResponse> {
        return tripService.getMyTripsAsDriver(username).map { mapToTripResponse(it, false) }
    }

    fun getMyTripsAsPassenger(username: String): List<TripResponse> =
            tripService.getMyTripsAsPassenger(username).map { mapToTripResponse(it, true) }

    fun findAllTripsByDriverId(id: Long) =
            tripService.findAllTripsByDriverId(id).map { mapToTripResponse(it, false) }


    private fun mapToTripResponse(trip: Trip, asPassenger: Boolean): TripResponse = with(trip) {
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
                allowedActions = getAllowedActions(trip, asPassenger))
    }

    //TODO: Move these to constants
    private fun getAllowedActions(trip: Trip, asPassenger: Boolean): List<String> = with(trip){
        return if (asPassenger) {
            when (status) {
                TripStatus.ACTIVE -> listOf(Actions.CANCEL_RESERVATION.name)
                TripStatus.FINISHED -> listOf(Actions.SUBMIT_RATING.name)
                else -> emptyList()
            }
        } else {
            if (status == TripStatus.ACTIVE) listOf(Actions.CANCEL_RIDE.name) else emptyList()
        }
    }

    private fun mapToTripDetailsResponse(trip: Trip): TripDetailsResponse = with(trip) {
        return TripDetailsResponse(isPetAllowed = isPetAllowed,
                isSmokingAllowed = isSmokingAllowed,
                hasAirCondition = hasAirCondition,
                additionalDescription = trip.additionalDescription)
    }

    private fun mapToUserShortResponse(user: User): UserShortResponse = with(user) {
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
//    fun getPastPublishedTripsByUser(userId: Long): List<TripResponse> =
//        tripService.getPastPublishedTripsByUser(userId).map { mapToTripResponse(it, false) }
//
//    fun findMyPastTripsAsPassenger(username: String) =
//            tripService.findMyPastTripsAsPassenger(username).map { mapToPastTripResponse(it, username) }

//    private fun mapToPastTripResponse(ride: Ride, username: String): PastTripResponse = with(ride) {
//        PastTripResponse(
//                tripId = id,
//                from = fromLocation.name,
//                to = destination.name,
//                pricePerHead = pricePerHead,
//                driver = mapToUserShortResponse(driver),
//                canSubmitRating = tripService.canSubmitRating(ride, username)
//        )
//    }
}