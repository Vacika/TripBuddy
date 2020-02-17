package com.project.najdiprevoz.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.project.najdiprevoz.enums.RequestStatus
import java.time.ZonedDateTime
import javax.persistence.*

@Entity
@Table(name = "ride_requests")
data class RideRequest(

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "requester_id")
        val requester: Member,

        @JsonBackReference
        @ManyToOne
        @JoinColumn(name = "ride_id", referencedColumnName = "id", nullable = false)
        val ride: Ride,

        @Column(name = "created_on")
        val createdOn: ZonedDateTime,

        @OneToOne(mappedBy = "rideRequest", optional = true, cascade = [CascadeType.ALL])
        @JsonManagedReference
        val rating: Rating? = null,

        @Enumerated(EnumType.STRING)
        @Column(name = "status", nullable = false)
        var status: RequestStatus = RequestStatus.PENDING

) : BaseEntity<Long>()