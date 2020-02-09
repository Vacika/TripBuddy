package com.project.najdiprevoz.domain

import java.time.ZonedDateTime
import javax.persistence.*

@Entity
@Table(name = "rides")
data class Ride(
        @Column(name = "created_on")
        private val createdOn: ZonedDateTime,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "from_location", referencedColumnName = "name")
        private val fromLocation: City,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "to_location", referencedColumnName = "name")
        private val destination: City,

        @Column(name = "departure_time")
        private val departureTime: ZonedDateTime,

        @Column(name = "total_seats")
        private val totalSeats: Int,

        @Column(name = "is_finished")
        private val finished: Boolean,

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "driver_id", nullable = false)
        private val driver: Member,

        @Column(name = "price_per_head")
        private val pricePerHead: Int,

        @Column(name = "description")
        private val additionalDescription: String?,

        @OneToMany(mappedBy = "ride")
        private val rideRequest: List<RideRequest>?

) : BaseEntity<Long>() {
    fun getAvailableSeats(): Int {
        if (this.rideRequest != null) {
            return this.totalSeats - this.rideRequest.filter { it.getStatus().name == "Approved" }.size
        }
        return this.totalSeats
    }

    fun canApproveRideRequest(): Boolean = this.getAvailableSeats() > 0

}