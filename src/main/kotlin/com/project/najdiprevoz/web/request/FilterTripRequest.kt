package com.project.najdiprevoz.web.request

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import java.util.*

class FilterTripRequest(
        val fromLocation: Long,
        val toLocation: Long,
        val requestedSeats: Int?,
        @DateTimeFormat(pattern = "MM-dd-yyyy")
        val departureDate: Date?
)
