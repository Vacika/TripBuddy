package com.project.najdiprevoz.mapper

import com.project.najdiprevoz.domain.Trip
import com.project.najdiprevoz.domain.User
import com.project.najdiprevoz.enums.Actions
import com.project.najdiprevoz.enums.TripStatus
import com.project.najdiprevoz.repositories.RatingViewRepository
import com.project.najdiprevoz.services.list.TripListService
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
                 private val tripListService: TripListService,
                 private val ratingViewRepository: RatingViewRepository) {

    fun findAllActiveTripsForToday(): List<TripResponse> {
        return tripListService.findAllActiveForToday().map { mapToTripResponse(it, false) }
    }

    fun findAllFiltered(req: FilterTripRequest): List<TripResponse> =
        tripListService.findAllFiltered(req)
                    .map { mapToTripResponse(it, false) }
                    .sortedByDescending { it.driver.rating }

    fun editTrip(tripId: Long, editTripRequest: EditTripRequest): TripResponse =
            mapToTripResponse(tripService.edit(tripId, editTripRequest), false)

    fun createNewTrip(createTripRequest: CreateTripRequest, username: String) =
            tripService.create(createTripRequest, username)

    fun cancelTrip(tripId: Long, username: String) =
            tripService.cancelTrip(tripId, username)

    fun findById(tripId: Long): TripResponse =
            mapToTripResponse(tripService.findById(tripId), false)

    fun getTripAdditionalInfo(tripId: Long): TripDetailsResponse {
        return mapToTripDetailsResponse(tripService.findById(tripId))
    }

    ///////////////////////////////////////////////////

    fun getMyTripsAsDriver(username: String): List<TripResponse> {
        return tripListService.getMyTripsAsDriver(username).map { mapToTripResponse(it, false) }
    }

    fun getMyTripsAsPassenger(username: String): List<TripResponse> =
            tripListService.getMyTripsAsPassenger(username).map { mapToTripResponse(it, true) }

    fun findAllTripsByDriverId(id: Long) =
            tripListService.findAllByDriverId(id).map { mapToTripResponse(it, false) }


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
}