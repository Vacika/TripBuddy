package com.project.najdiprevoz.enums

enum class NotificationAction(private val action: String) {
    APPROVE("APPROVE"),
    CANCEL_RESERVATION("CANCEL_RESERVATION"),
    MARK_AS_SEEN("MARK_AS_SEEN"),
    DENY("DENY"),
    SUBMIT_RATING("SUBMIT_RATING")
}