package com.project.najdiprevoz.enums


enum class NotificationType(private val type: String) {
    REQUEST_SENT("REQUEST_SENT"),
    REQUEST_DENIED("REQUEST_DENIED"),
    REQUEST_APPROVED("REQUEST_APPROVED"),
    REQUEST_CANCELLED("REQUEST_CANCELLED"),
    TRIP_CANCELLED("TRIP_CANCELLED"),
    RATING_SUBMITTED("RATING_GIVEN"),
    REQUEST_EXPIRED("REQUEST_EXPIRED"),
    RATING_ALLOWED("RATING_ALLOWED")
}