package com.project.najdiprevoz.web.request.create

import java.time.ZonedDateTime

class CreateTripRequest(val fromLocation: Long,
                        val destination: Long,
                        val departureTime: ZonedDateTime,
                        val totalSeats: Int,
                        val driverId: Long,
                        val pricePerHead: Int,
                        val additionalDescription: String?,
                        val hasAirCondition: Boolean,
                        val maxTwoBackseat: Boolean,
                        val smokingAllowed: Boolean,
                        val petAllowed: Boolean)