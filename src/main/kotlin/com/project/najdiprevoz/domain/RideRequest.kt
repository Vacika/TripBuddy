package com.project.najdiprevoz.domain

import java.time.ZonedDateTime
import javax.persistence.*

@Entity
@Table(name = "ride_requests")
data class RideRequest(

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "requester_id")
        private val requester: Member,

        @ManyToOne(fetch=FetchType.LAZY)
        @JoinColumn(name = "ride_id", referencedColumnName = "id", nullable = false)
        private val ride: Ride,

        @ManyToOne
        @JoinColumn(name = "city_from", referencedColumnName = "id", nullable = false)
        private val cityFrom: City,

        @ManyToOne
        @JoinColumn(name = "city_to", referencedColumnName = "id", nullable = false)
        private val cityTo: City,

        @Column(name="created_on")
        private val createdOn: ZonedDateTime,

        @Enumerated(EnumType.STRING)
        @Column(name="status")
        private val status: RequestRideStatus

) : BaseEntity<Long>() {

        fun getStatus() = status
}