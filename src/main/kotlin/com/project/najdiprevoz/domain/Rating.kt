package com.project.najdiprevoz.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
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

        @JsonIgnore
        @ManyToOne
        @JoinColumn(name="rated_user")
        val ratedUser: User = rideRequest.ride.driver
) {
    fun getAuthor(): User = rideRequest.requester
    fun getDriver(): User = rideRequest.ride.driver

    override fun toString(): String = "Rating id:[${id}], Ride Request id:[${rideRequest.id}], rating: [$rating], date: [$dateSubmitted]"

}