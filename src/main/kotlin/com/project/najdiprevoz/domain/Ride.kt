package com.project.najdiprevoz.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.project.najdiprevoz.enums.RequestStatus
import com.project.najdiprevoz.enums.RideStatus
import com.project.najdiprevoz.web.response.TripResponse
import java.time.ZonedDateTime
import javax.persistence.*

//TODO: Implement Builder Pattern
@Entity
@Table(name = "rides")
data class Ride(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0L,

        @Column(name = "created_on")
        val createdOn: ZonedDateTime,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "from_location", referencedColumnName = "id")
        val fromLocation: City,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "to_location", referencedColumnName = "id")
        val destination: City,

        @Column(name = "departure_time")
        var departureTime: ZonedDateTime,

        @Column(name = "total_seats_offered")
        val totalSeatsOffered: Int,

        @JsonBackReference
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "driver_id")
        var driver: AppUser,

        @Column(name = "price_per_head")
        var pricePerHead: Int,

        @Column(name = "description")
        var additionalDescription: String?,

        @JsonManagedReference
        @OneToMany(mappedBy = "ride", targetEntity = RideRequest::class, fetch = FetchType.EAGER, cascade = [CascadeType.ALL]) //TODO: Change this to LAZY OR EAGER?
        var rideRequests: List<RideRequest> = listOf(),

        @Enumerated(EnumType.STRING)
        @Column(name = "status")
        var status: RideStatus,

        @Column(name = "is_smoking_allowed")
        val isSmokingAllowed: Boolean = false,

        @Column(name = "is_pet_allowed")
        val isPetAllowed: Boolean = false,

        @Column(name = "max_two_backseat")
        val maxTwoBackSeat: Boolean = false,

        @Column(name = "has_air_condition")
        val hasAirCondition: Boolean = false) {

    fun getAvailableSeats(): Int = this.totalSeatsOffered - this.rideRequests.filter { it.status == RequestStatus.APPROVED }.sumBy { it.requestedSeats }

    fun canApproveRideRequest(): Boolean = this.getAvailableSeats() > 0

    fun isFinished(): Boolean = this.status == RideStatus.FINISHED

    fun getDriverFullName() = this.driver.getFullName()

    fun mapToTripResponse(): TripResponse {
        return TripResponse(id = id,
                from = fromLocation.name,
                to = destination.name,
                departureTime = departureTime,
                availableSeats = getAvailableSeats(),
                pricePerHead = pricePerHead,
                totalSeats = totalSeatsOffered,
                driver = driver.mapToUserShortResponse(),
                maxTwoBackSeat = maxTwoBackSeat)
    }

    @Override
    override fun toString(): String = ""
}