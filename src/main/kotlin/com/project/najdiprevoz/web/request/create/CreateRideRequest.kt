package com.project.najdiprevoz.web.request.create

import com.project.najdiprevoz.domain.City
import java.time.ZonedDateTime

class CreateRideRequest(val fromLocation: City,
                        val destination: City,
                        val departureTime: ZonedDateTime,
                        val totalSeats: Int,
                        val driverId: Long,
                        val pricePerHead: Int,
                        val additionalDescription: String?)