package com.project.najdiprevoz.domain

import com.project.najdiprevoz.enums.RequestRideStatus
import java.time.ZonedDateTime
import javax.persistence.*

@Entity
@Table(name = "ride_requests")
data class RideRequest(

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "requester_id")
        val requester: Member,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "ride_id", referencedColumnName = "id", nullable = false)
        val ride: Ride,

        @ManyToOne
        @JoinColumn(name = "city_from")
        val cityFrom: City,

        @ManyToOne
        @JoinColumn(name = "city_to")
        val cityTo: City,

        @Column(name = "created_on")
        val createdOn: ZonedDateTime,

        @Enumerated(EnumType.STRING)
        @Column(name = "status")
        val status: RequestRideStatus

) : BaseEntity<Long>()