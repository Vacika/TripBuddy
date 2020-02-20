package com.project.najdiprevoz.web.request

import java.util.*

class FilterTripRequest(
        val fromAddress: String?,
        val toAddress: String?,
        val requestedSeats: Int?,
        val departureTime: String?,
        val departureDay: Date?
)
