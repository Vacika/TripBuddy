package com.project.najdiprevoz.enums

enum class RideRequestStatus(private val status: String) {
    PENDING("Pending"),
    APPROVED("Approved"),
    DENIED("Denied"),
    CANCELLED("Cancelled"),
    RIDE_CANCELLED("Ride Cancelled"),
    EXPIRED("Expired")
}
