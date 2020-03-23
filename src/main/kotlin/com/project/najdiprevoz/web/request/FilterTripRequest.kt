package com.project.najdiprevoz.web.request

import java.time.ZonedDateTime

class FilterTripRequest(
        val fromLocation: Long,
        val toLocation: Long,
        val requestedSeats: Int?,
        val departureDate: ZonedDateTime?
)
