package com.project.najdiprevoz.web.request

import java.time.ZonedDateTime
import java.util.*

class FilterTripRequest(
        val fromAddress: String?,
        val toAddress: String?,
        val requestedSeats: Int?,
        val departure: ZonedDateTime?
)
