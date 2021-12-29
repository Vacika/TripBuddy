package com.project.najdiprevoz.enums

enum class ReservationStatus(private val status: String) {
    PENDING("Pending"),
    APPROVED("Approved"),
    DENIED("Denied"),
    CANCELLED("Cancelled"),
    TRIP_CANCELLED("Trip Cancelled"),
    EXPIRED("Expired"),
    FINISHED("Finished")
}
