package com.project.najdiprevoz.domain

import com.project.najdiprevoz.enums.NotificationType
import java.time.ZonedDateTime
import javax.persistence.*

@Entity
@Table(name = "notifications")
data class Notification(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0L,

        val createdOn: ZonedDateTime,

        @ManyToOne
        @JoinColumn(name = "ride_request_id", referencedColumnName = "id", nullable = true)
        val rideRequest: RideRequest,

        @Enumerated(EnumType.STRING)
        val type: NotificationType,

        val actionsAvailable: String? = "MARK_AS_SEEN",

        @ManyToOne
        @JoinColumn(name = "from_id", referencedColumnName = "id", nullable = true)
        val from: User,

        @ManyToOne
        @JoinColumn(name = "to_id", referencedColumnName = "id", nullable = true)
        val to: User,

        val seen: Boolean = false)

