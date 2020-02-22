package com.project.najdiprevoz.enums

enum class NotificationActions(private val action: String) {
    APPROVE("Approve"), CANCEL("Cancel"),
    MARK_AS_SEEN("Mark as seen"), DENY("Deny")
}