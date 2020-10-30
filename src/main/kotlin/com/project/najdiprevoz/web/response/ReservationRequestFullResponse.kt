package com.project.najdiprevoz.web.response

import java.time.ZonedDateTime

class ReservationRequestFullResponse(
        val id: Long,
        val requesterName: String,
        val tripId: Long,
        val allowedActions: List<String>?,
        val fromLocation: String,
        val toLocation: String,
        val departureTime: ZonedDateTime,
        val driverName: String,
        val requestStatus: String,
        val tripStatus: String,
        val requestedSeats: Int,
        val additionalDescription: String?
)
