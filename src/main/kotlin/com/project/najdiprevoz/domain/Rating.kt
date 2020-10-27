package com.project.najdiprevoz.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
import com.project.najdiprevoz.web.response.RatingResponse
import java.time.ZonedDateTime
import javax.persistence.*

@Entity
@Table(name = "ratings")
data class Rating(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0L,

        @JsonBackReference
        @OneToOne(optional = false)
        @JoinColumn(name = "reservation_request_id", referencedColumnName = "id", nullable = false, unique = true)
        val reservationRequest: ReservationRequest,

        @Column(name = "note")
        val note: String?,

        @Column(name = "date_submitted")
        val dateSubmitted: ZonedDateTime,

        @Column(name = "rating")
        val rating: Int) {

    @JsonIgnore
    fun getAuthor(): User = reservationRequest.requester

    @JsonIgnore
    fun getDriver(): User = reservationRequest.trip.driver

    override fun toString(): String = "Rating id:[${id}], Reservation Request id:[${reservationRequest.id}], rating: [$rating], date: [$dateSubmitted]"

    fun mapToRatingResponse(): RatingResponse = RatingResponse(
                id = id,
                rating = rating,
                note = note,
                rideId = reservationRequest.trip.id,
                fromFullName = getAuthor().getFullName(),
                fromId = getAuthor().id,
                fromProfilePic = getAuthor().profilePhoto,
                rideDate = reservationRequest.trip.departureTime,
                rideFrom = reservationRequest.trip.fromLocation.name,
                rideTo = reservationRequest.trip.destination.name
        )
}