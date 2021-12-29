package com.project.najdiprevoz.mapper

import com.project.najdiprevoz.domain.ReservationRequest
import com.project.najdiprevoz.domain.User
import com.project.najdiprevoz.enums.ReservationStatus
import com.project.najdiprevoz.repositories.RatingViewRepository
import com.project.najdiprevoz.services.list.ReservationRequestListService
import com.project.najdiprevoz.services.ReservationRequestService
import com.project.najdiprevoz.web.request.create.CreateReservationRequestForTrip
import com.project.najdiprevoz.web.response.ReservationRequestFullResponse
import com.project.najdiprevoz.web.response.ReservationRequestResponse
import com.project.najdiprevoz.web.response.UserShortResponse
import org.springframework.stereotype.Service

@Service
class ReservationRequestMapper(
    private val service: ReservationRequestService,
    private val listService: ReservationRequestListService,
    private val ratingViewRepository: RatingViewRepository
) {

    fun findById(id: Long): ReservationRequestResponse =
        mapToReservationRequestResponse(service.findById(id), service.getAvailableActions(id, true))

    fun getAllRequestByTripId(tripId: Long) =
        listService.getAllRequestsByTripId(tripId)
            .map { mapToReservationRequestResponse(it, service.getAvailableActions(it.id, false)) }

    fun getSentReservationRequests(username: String) =
        listService.getSentReservationRequests(username)
            .map { mapToReservationRequestFullResponse(it, service.getAvailableActions(it.id, true)) }

    fun getReceivedReservationRequests(username: String) =
        listService.getReceivedReservationRequests(username)
            .map { mapToReservationRequestFullResponse(it, service.getAvailableActions(it.id, false)) }

    fun getRequestsForTripByStatus(tripId: Long, status: ReservationStatus): List<ReservationRequestResponse> =
        listService.getRequestsForTripByStatus(tripId, status)
            .map { mapToReservationRequestResponse(it, service.getAvailableActions(it.id, true)) }

    fun changeStatus(id: Long, newStatus: ReservationStatus) =
        service.changeStatus(id, newStatus)

    fun addNewReservationRequest(req: CreateReservationRequestForTrip, username: String) =
        service.create(req, username)


    private fun mapToReservationRequestResponse(
        reservationRequest: ReservationRequest,
        allowedActions: List<String>?
    ): ReservationRequestResponse = with(reservationRequest) {
        ReservationRequestResponse(
            id = id,
            requester = mapToUserShortResponse(requester),
            tripId = trip.id,
            allowedActions = allowedActions
        )
    }

    private fun mapToReservationRequestFullResponse(
        reservationRequest: ReservationRequest,
        allowedActions: List<String>?
    ): ReservationRequestFullResponse = with(reservationRequest) {
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