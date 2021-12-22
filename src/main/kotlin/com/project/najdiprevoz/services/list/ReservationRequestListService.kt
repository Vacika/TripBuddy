package com.project.najdiprevoz.services.list

import com.project.najdiprevoz.domain.ReservationRequest
import com.project.najdiprevoz.enums.ReservationStatus
import com.project.najdiprevoz.repositories.ReservationRequestRepository
import org.springframework.stereotype.Service

@Service
class ReservationRequestListService(private val repository: ReservationRequestRepository) {

    fun findAll(): List<ReservationRequest> =
        repository.findAll()

    fun getAllRequestsByTripId(rideId: Long): List<ReservationRequest> =
        repository.findAllByTripId(rideId)

    //Gets request sent from the user
    fun getSentReservationRequests(username: String) =
        repository.findAllByRequesterUsername(username = username)
            .sortedBy { it.status.ordinal }

    //Gets received requests for user
    fun getReceivedReservationRequests(username: String) =
        repository.findReceivedRequests(username = username)
            .sortedBy { it.status.ordinal }

    fun getRequestsForRideByStatus(rideId: Long, status: ReservationStatus): List<ReservationRequest> =
        repository.findAllByStatusAndTripId(status, rideId)
            .sortedBy { it.status.ordinal }
}