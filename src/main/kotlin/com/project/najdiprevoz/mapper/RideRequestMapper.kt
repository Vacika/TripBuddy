package com.project.najdiprevoz.mapper

import com.project.najdiprevoz.domain.RideRequest
import com.project.najdiprevoz.domain.User
import com.project.najdiprevoz.enums.RideRequestStatus
import com.project.najdiprevoz.repositories.RatingViewRepository
import com.project.najdiprevoz.services.RideRequestService
import com.project.najdiprevoz.web.request.create.CreateRequestForTrip
import com.project.najdiprevoz.web.response.RideRequestFullResponse
import com.project.najdiprevoz.web.response.RideRequestResponse
import com.project.najdiprevoz.web.response.UserShortResponse
import org.springframework.stereotype.Service
import java.time.format.DateTimeFormatter

@Service
class RideRequestMapper(private val service: RideRequestService,
                        private val ratingViewRepository: RatingViewRepository) {

    fun findById(id: Long): RideRequestResponse =
            mapToRideRequestResponse(service.findById(id), getAvailableActions(id, true))

    fun getAllRequestByTripId(rideId: Long) =
            service.getAllRequestsByTripId(rideId)
                    .map { mapToRideRequestResponse(it, getAvailableActions(it.id, false)) }

    fun getSentRideRequests(username: String) =
            service.getSentRideRequests(username)
                    .map { mapToRideRequestFullResponse(it, getAvailableActions(it.id, true)) }

    fun getReceivedRideRequests(username: String) =
            service.getReceivedRideRequests(username)
                    .map { mapToRideRequestFullResponse(it, getAvailableActions(it.id, false)) }

    fun getRequestsForRideByStatus(rideId: Long, status: RideRequestStatus): List<RideRequestResponse> =
            service.getRequestsForRideByStatus(rideId, status)
                    .map { mapToRideRequestResponse(it, getAvailableActions(it.id, true)) }

    fun changeStatus(id: Long, newStatus: RideRequestStatus) =
            service.changeStatus(id, newStatus)

    fun addNewRideRequest(req: CreateRequestForTrip, username: String) =
            service.addNewRideRequest(req, username)

    private fun getAvailableActions(requestId: Long, forRequester: Boolean): List<String> =
            service.getAvailableActions(requestId, forRequester)

    private fun mapToRideRequestResponse(rideRequest: RideRequest, allowedActions: List<String>?): RideRequestResponse = with(rideRequest) {
        RideRequestResponse(
                id = id,
                requester = mapToUserShortResponse(requester),
                tripId = ride.id,
                allowedActions = allowedActions
        )
    }

    private fun mapToRideRequestFullResponse(rideRequest: RideRequest,
                                             allowedActions: List<String>?): RideRequestFullResponse = with(rideRequest) {
        RideRequestFullResponse(
                id = id,
                allowedActions = allowedActions,
                requesterName = requester.getFullName(),
                tripId = ride.id,
                departureTime = ride.departureTime,
                requestedSeats = rideRequest.requestedSeats,
                fromLocation = ride.fromLocation.name,
                toLocation = ride.destination.name,
                driverName = ride.driver.getFullName(),
                requestStatus = status.toString(),
                rideStatus = ride.status.toString()
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