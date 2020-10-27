package com.project.najdiprevoz.mapper

import com.project.najdiprevoz.domain.ReservationRequest
import com.project.najdiprevoz.domain.User
import com.project.najdiprevoz.enums.ReservationStatus
import com.project.najdiprevoz.repositories.RatingViewRepository
import com.project.najdiprevoz.services.ReservationRequestService
import com.project.najdiprevoz.web.request.create.CreateRequestForTrip
import com.project.najdiprevoz.web.response.ReservationRequestFullResponse
import com.project.najdiprevoz.web.response.ReservationRequestResponse
import com.project.najdiprevoz.web.response.UserShortResponse
import org.springframework.stereotype.Service

@Service
class ReservationRequestMapper(private val service: ReservationRequestService,
                               private val ratingViewRepository: RatingViewRepository) {

    fun findById(id: Long): ReservationRequestResponse =
            mapToReservationRequestResponse(service.findById(id), getAvailableActions(id, true))

    fun getAllRequestByTripId(rideId: Long) =
            service.getAllRequestsByTripId(rideId)
                    .map { mapToReservationRequestResponse(it, getAvailableActions(it.id, false)) }

    fun getSentReservationRequests(username: String) =
            service.getSentReservationRequests(username)
                    .map { mapToReservationRequestFullResponse(it, getAvailableActions(it.id, true)) }

    fun getReceivedReservationRequests(username: String) =
            service.getReceivedReservationRequests(username)
                    .map { mapToReservationRequestFullResponse(it, getAvailableActions(it.id, false)) }

    fun getRequestsForRideByStatus(rideId: Long, status: ReservationStatus): List<ReservationRequestResponse> =
            service.getRequestsForRideByStatus(rideId, status)
                    .map { mapToReservationRequestResponse(it, getAvailableActions(it.id, true)) }

    fun changeStatus(id: Long, newStatus: ReservationStatus) =
            service.changeStatus(id, newStatus)

    fun addNewReservationRequest(req: CreateRequestForTrip, username: String) =
            service.addNewReservationRequest(req, username)

    private fun getAvailableActions(requestId: Long, forRequester: Boolean): List<String> =
            service.getAvailableActions(requestId, forRequester)

    private fun mapToReservationRequestResponse(reservationRequest: ReservationRequest, allowedActions: List<String>?): ReservationRequestResponse = with(reservationRequest) {
        ReservationRequestResponse(
                id = id,
                requester = mapToUserShortResponse(requester),
                tripId = trip.id,
                allowedActions = allowedActions
        )
    }

    private fun mapToReservationRequestFullResponse(reservationRequest: ReservationRequest,
                                             allowedActions: List<String>?): ReservationRequestFullResponse = with(reservationRequest) {
        ReservationRequestFullResponse(
                id = id,
                allowedActions = allowedActions,
                requesterName = requester.getFullName(),
                tripId = trip.id,
                departureTime = trip.departureTime,
                requestedSeats = reservationRequest.requestedSeats,
                fromLocation = trip.fromLocation.name,
                toLocation = trip.destination.name,
                driverName = trip.driver.getFullName(),
                requestStatus = status.toString(),
                tripStatus = trip.status.toString(),
                additionalDescription = reservationRequest.additionalDescription
        )
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