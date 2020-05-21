package com.project.najdiprevoz.web.response

import java.time.ZonedDateTime

class TripResponse(val id: Long,
                   val driver: UserShortResponse,
                   val from: String,
                   val to: String,
                   val availableSeats: Int,
                   val status: String,
                   val totalSeats: Int,
                   val departureTime: ZonedDateTime,
                   val pricePerHead: Int,
                   val maxTwoBackSeat: Boolean,
                   val allowedActions: List<String>?) {
}

class PastTripResponse(
        val tripId: Long,
        val driver: UserShortResponse,
        val from: String,
        val to: String,
        val pricePerHead: Int,
        val canSubmitRating: Boolean)