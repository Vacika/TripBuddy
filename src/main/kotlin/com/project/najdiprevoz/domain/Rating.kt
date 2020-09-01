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
        @JoinColumn(name = "ride_request_id", referencedColumnName = "id", nullable = false, unique = true)
        val rideRequest: RideRequest,

        @Column(name = "note")
        val note: String?,

        @Column(name = "date_submitted")
        val dateSubmitted: ZonedDateTime,

        @Column(name = "rating")
        val rating: Int,

        @JsonBackReference
        @ManyToOne
        @JoinColumn(name = "rated_user")
        val ratedUser: User = rideRequest.ride.driver) {

    @JsonIgnore
    fun getAuthor(): User = rideRequest.requester

    @JsonIgnore
    fun getDriver(): User = rideRequest.ride.driver

    override fun toString(): String = "Rating id:[${id}], Ride Request id:[${rideRequest.id}], rating: [$rating], date: [$dateSubmitted]"

    fun mapToRatingResponse(): RatingResponse = RatingResponse(
                id = id,
                rating = rating,
                note = note,
                rideId = rideRequest.ride.id,
                fromFullName = getAuthor().getFullName(),
                fromId = getAuthor().id,
                fromProfilePic = getAuthor().profilePhoto,
                rideDate = rideRequest.ride.departureTime,
                rideFrom = rideRequest.ride.fromLocation.name,
                rideTo = rideRequest.ride.destination.name
        )
}