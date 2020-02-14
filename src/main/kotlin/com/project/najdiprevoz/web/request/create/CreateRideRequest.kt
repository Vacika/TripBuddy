package com.project.najdiprevoz.web.request.create

import java.time.ZonedDateTime

class CreateRideRequest(val fromLocation: String,
                        val destination: String,
                        val departureTime: ZonedDateTime,
                        val totalSeats: Int,
                        val driverId: Long,
                        val pricePerHead: Int,
                        val additionalDescription: String?)