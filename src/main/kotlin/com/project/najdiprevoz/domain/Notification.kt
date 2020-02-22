package com.project.najdiprevoz.domain

import com.project.najdiprevoz.enums.Actions
import com.project.najdiprevoz.enums.NotificationType
import java.time.ZonedDateTime
import javax.persistence.*

@Entity
@Table(name = "notifications")
data class Notification(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0L,

        @Column(name="created_on")
        val createdOn: ZonedDateTime,

        @ManyToOne
        @JoinColumn(name = "ride_request_id", referencedColumnName = "id", nullable = true)
        val rideRequest: RideRequest,

        @Enumerated(EnumType.STRING)
        @Column(name="type")
        val type: NotificationType,

        @Column(name="actions_available")
        val actionsAvailable: String = Actions.MARK_AS_SEEN.name,

        @ManyToOne
        @JoinColumn(name = "from_id", referencedColumnName = "id", nullable = true)
        val from: User,

        @ManyToOne
        @JoinColumn(name = "to_id", referencedColumnName = "id", nullable = true)
        val to: User,

        @Column(name="seen")
        val seen: Boolean = false)

