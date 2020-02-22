package com.project.najdiprevoz.web.request

import java.time.ZonedDateTime

class FilterTripRequest(
        val fromAddress: String?,
        val toAddress: String?,
        val requestedSeats: Int?,
        val departure: ZonedDateTime?
)
