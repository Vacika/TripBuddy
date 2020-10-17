package com.project.najdiprevoz.web.response

class RideRequestFullResponse(
        val id: Long,
        val requesterName: String,
        val tripId: Long,
        val allowedActions: List<String>?,
        val fromLocation: String,
        val toLocation: String,
        val departureTime: String,
        val driverName: String,
        val requestStatus: String,
        val rideStatus: String,
        val requestedSeats: Int
)
