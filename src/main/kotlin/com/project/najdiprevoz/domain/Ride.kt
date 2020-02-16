package com.project.najdiprevoz.domain

import com.project.najdiprevoz.enums.RequestStatus
import java.time.ZonedDateTime
import javax.persistence.*

//TODO: Implement Builder Pattern

@Entity
@Table(name = "rides")
data class Ride(
        @Column(name = "created_on")
        val createdOn: ZonedDateTime,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "from_location", referencedColumnName = "id")
        val fromLocation: City,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "to_location", referencedColumnName = "id")
        val destination: City,

        @Column(name = "departure_time")
        val departureTime: ZonedDateTime,

        @Column(name = "total_seats_offered")
        val totalSeatsOffered: Int,

        @Column(name = "is_finished")
        val finished: Boolean,

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "driver_id", nullable = false)
        val driver: Member,

        @Column(name = "price_per_head")
        val pricePerHead: Int,

        @Column(name = "description")
        val additionalDescription: String?,

        @OneToMany(mappedBy = "ride")
        val rideRequests: List<RideRequest>?,

        @OneToMany(mappedBy = "ride", fetch = FetchType.LAZY)
        val rating: List<Rating>?

) : BaseEntity<Long>() {
    fun getAvailableSeats(): Int {
        if (this.rideRequests != null) {
            return this.totalSeatsOffered - this.rideRequests.filter { it.status == RequestStatus.APPROVED }.size
        }
        return this.totalSeatsOffered
    }

    fun canApproveRideRequest(): Boolean = this.getAvailableSeats() > 0

    @Override
    override fun toString(): String {
        return ""
    }

}