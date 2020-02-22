package com.project.najdiprevoz.domain

import com.project.najdiprevoz.enums.NotificationActions
import com.project.najdiprevoz.enums.NotificationType
import java.time.ZonedDateTime
import javax.persistence.*

@Entity
@Table(name = "notifications")
data class Notification(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0L,

        @Column(name = "created_on")
        val createdOn: ZonedDateTime,

        @ManyToOne
        @JoinColumn(name = "ride_request_id", referencedColumnName = "id", nullable = true)
        val rideRequest: RideRequest,

        @Enumerated(EnumType.STRING)
        @Column(name = "type")
        val type: NotificationType,

        @Enumerated(EnumType.STRING)
        @ElementCollection(targetClass = NotificationActions::class, fetch = FetchType.EAGER)
        var actions: List<NotificationActions> = listOf(NotificationActions.MARK_AS_SEEN),

        @ManyToOne
        @JoinColumn(name = "from_id", referencedColumnName = "id", nullable = true)
        val from: User,

        @ManyToOne
        @JoinColumn(name = "to_id", referencedColumnName = "id", nullable = true)
        val to: User,

        @Column(name = "seen")
        var seen: Boolean = false) {

    fun markAsSeen(): Notification {
        seen = true
        removeAction(NotificationActions.MARK_AS_SEEN)
        return this
    }

    private fun removeAction(action: NotificationActions) {
        actions = actions.minus(action)
    }
}

