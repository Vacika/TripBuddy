package com.project.najdiprevoz.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.project.najdiprevoz.enums.ReservationStatus
import java.time.ZonedDateTime
import javax.persistence.*

@Entity
@Table(name = "reservation_requests")
class ReservationRequest(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0L,

        @JsonBackReference
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "requester_id")
        val requester: User,

        @JsonBackReference
        @ManyToOne
        @JoinColumn(name = "trip_id", referencedColumnName = "id", nullable = false)
        val trip: Trip,

        @Column(name = "created_on", nullable = false)
        val createdOn: ZonedDateTime,

        @Column(name = "requested_seats", nullable = false)
        val requestedSeats: Int,

        @Column(name = "additional_description", nullable = true)
        val additionalDescription: String?,

        @JsonManagedReference
        @OneToOne(mappedBy = "reservationRequest", optional = true)
        val rating: Rating? = null,

        @Enumerated(EnumType.STRING)
        @Column(name = "status", nullable = false)
        var status: ReservationStatus = ReservationStatus.PENDING
) {
    fun getRequesterFullName() = requester.getFullName()

    fun changeStatus(newStatus: ReservationStatus): ReservationRequest {
        this.status = newStatus
        return this
    }
}