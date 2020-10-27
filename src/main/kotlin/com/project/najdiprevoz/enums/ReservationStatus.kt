package com.project.najdiprevoz.enums

enum class ReservationStatus(private val status: String) {
    PENDING("Pending"),
    APPROVED("Approved"),
    DENIED("Denied"),
    CANCELLED("Cancelled"),
    RIDE_CANCELLED("Ride Cancelled"),
    EXPIRED("Expired")
}
