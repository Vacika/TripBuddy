package com.project.najdiprevoz.services.list

import com.project.najdiprevoz.domain.ReservationRequest
import com.project.najdiprevoz.enums.ReservationStatus
import com.project.najdiprevoz.repositories.ReservationRequestRepository
import org.springframework.stereotype.Service

@Service
class ReservationRequestListService(private val repository: ReservationRequestRepository) {

    fun findAll(): List<ReservationRequest> =
        repository.findAll()
            .sortedBy { it.status.ordinal }

    fun getAllRequestsByTripId(tripId: Long): List<ReservationRequest> =
        repository.findAllByTripId(tripId)
            .sortedBy { it.status.ordinal }


    //Gets request sent from the user
    fun getSentReservationRequests(username: String) =
        repository.findAllByRequesterUsername(username = username)
            .sortedBy { it.status.ordinal }

    //Gets received requests for user
    fun getReceivedReservationRequests(username: String) =
        repository.findReceivedRequests(username = username)
            .sortedBy { it.status.ordinal }

    fun getRequestsForTripByStatus(tripId: Long, status: ReservationStatus): List<ReservationRequest> =
        repository.findAllByStatusAndTripId(status, tripId)
            .sortedBy { it.status.ordinal }
}