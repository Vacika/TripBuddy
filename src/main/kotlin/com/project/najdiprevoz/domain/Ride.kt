package com.project.najdiprevoz.domain

import java.time.ZonedDateTime
import javax.persistence.*

@Entity
@Table(name = "rides")
data class Ride(
        @Column(name = "created_on")
        val createdOn: ZonedDateTime,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name="from_location", referencedColumnName = "id")
        val fromLocation: City,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "to_location", referencedColumnName = "id")
        val destination: City,

        @Column(name = "departure_time")
        val departureTime: ZonedDateTime,

        @Column(name = "total_seats")
        val totalSeats: Int,

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
        val rideRequest: List<RideRequest>?,

        @OneToMany(mappedBy = "ride", fetch = FetchType.LAZY)
        val rating: List<Rating>?

) : BaseEntity<Long>() {
    fun getAvailableSeats(): Int {
        if (this.rideRequest != null) {
            return this.totalSeats - this.rideRequest.filter { it.status.name == "Approved" }.size
        }
        return this.totalSeats
    }
    fun canApproveRideRequest(): Boolean = this.getAvailableSeats() > 0

    @Override
    override fun toString(): String{
        return ""
    }

}