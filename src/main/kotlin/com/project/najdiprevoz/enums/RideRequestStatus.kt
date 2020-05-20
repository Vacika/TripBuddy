package com.project.najdiprevoz.enums

enum class RideRequestStatus(private val status: String) {
    APPROVED("Approved"),
    DENIED("Denied"),
    PENDING("Pending"),
    CANCELLED("Cancelled"),
    RIDE_CANCELLED("Ride Cancelled"),
    EXPIRED("Expired")
}
