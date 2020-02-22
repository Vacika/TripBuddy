package com.project.najdiprevoz.domain

import java.time.ZonedDateTime
import javax.persistence.*

@Entity
@Table(name = "notifications")
data class Notification(val createdOn: ZonedDateTime,
                        @ManyToOne
                        @JoinColumn(name = "ride_request_id", referencedColumnName = "id", nullable = true)
                        val rideRequest: RideRequest,

                        @Enumerated(EnumType.STRING)
                        val type: NotificationType,

                        val actionsAvailable: String? = "MARK_AS_SEEN",
                        @ManyToOne
                        @JoinColumn(name = "from_id", referencedColumnName = "id", nullable = true)
                        val from: Member,
                        @ManyToOne
                        @JoinColumn(name = "to_id", referencedColumnName = "id", nullable = true)
                        val to: Member,
                        val seen: Boolean = false) : BaseEntity<Long>()

enum class NotificationType(private val type: String) {
    REQUEST_SENT("REQUEST_SENT"), REQUEST_DENIED("REQUEST_DENIED"),
    REQUEST_APPROVED("REQUEST_APPROVED"), REQUEST_CANCELLED("REQUEST_CANCELLED"),
    RIDE_CANCELLED("RIDE_CANCELLED"), RATING_SUBMITTED("RATING_GIVEN"),
    REQUEST_EXPIRED("Request expired")
}

enum class Actions(private val action: String) {
    APPROVE("Approve"), CANCEL("Cancel"),
    MARK_AS_SEEN("Mark as seen"), DENY("Deny")
}

