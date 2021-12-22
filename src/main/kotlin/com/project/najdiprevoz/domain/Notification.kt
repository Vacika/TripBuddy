package com.project.najdiprevoz.domain

import com.project.najdiprevoz.enums.NotificationAction
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
        @JoinColumn(name = "reservation_request_id", referencedColumnName = "id", nullable = true)
        val reservationRequest: ReservationRequest,

        @Enumerated(EnumType.STRING)
        @Column(name = "type")
        val type: NotificationType,

        @Enumerated(EnumType.STRING)
        @ElementCollection(targetClass = NotificationAction::class, fetch = FetchType.LAZY)
        var actions: MutableList<NotificationAction> = mutableListOf(NotificationAction.MARK_AS_SEEN),

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
        removeAction(NotificationAction.MARK_AS_SEEN)
        return this
    }

    private fun removeAction(action: NotificationAction) {
        actions.removeIf { it.name == action.name }
    }

    fun removeAllActions(): Notification {
        this.actions = mutableListOf()
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Notification

        if (id != other.id) return false
        return true
    }
}

