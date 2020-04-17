package com.project.najdiprevoz.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.project.najdiprevoz.enums.RequestStatus
import com.project.najdiprevoz.web.response.RideRequestResponse
import java.time.ZonedDateTime
import javax.persistence.*

@Entity
@Table(name = "ride_requests")
data class RideRequest(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0L,

        @JsonBackReference
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "requester_id")
        val requester: User,

        @JsonBackReference
        @ManyToOne
        @JoinColumn(name = "ride_id", referencedColumnName = "id", nullable = false)
        val ride: Ride,

        @Column(name = "created_on", nullable = false)
        val createdOn: ZonedDateTime,

        @Column(name = "requested_seats", nullable = false)
        val requestedSeats: Int,

        @Column(name = "additional_description", nullable = true)
        val additionalDescription: String?,

        @JsonManagedReference
        @OneToOne(mappedBy = "rideRequest", optional = true)
        val rating: Rating? = null,

        @Enumerated(EnumType.STRING)
        @Column(name = "status", nullable = false)
        var status: RequestStatus = RequestStatus.PENDING
) {
    fun getRequesterFullName() = requester.getFullName()

    fun mapToRideRequestResponse(): RideRequestResponse {
        return RideRequestResponse(
                id = id,
                profilePhoto = requester.profilePhoto,
                requester = requester.mapToUserShortResponse(),
                tripId = ride.id
        )
    }
}