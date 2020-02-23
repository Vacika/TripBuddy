package com.project.najdiprevoz.web.response

import java.time.ZonedDateTime

class TripResponse(val id: Long,
                   val driver: UserResponse,
                   val from: String,
                   val to: String,
                   val availableSeats: Int,
                   val departureTime: ZonedDateTime,
                   val pricePerHead: Int) {
}