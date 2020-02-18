package com.project.najdiprevoz.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import java.time.ZonedDateTime
import javax.persistence.*

@Entity
@Table(name = "ratings")
data class Rating(
        @JsonBackReference
        @OneToOne(optional = false)
        @JoinColumn(name = "ride_request_id", referencedColumnName = "id", nullable = false, unique = true)
        val rideRequest: RideRequest,

        @Column(name = "note")
        val note: String?,

        @Column(name = "date_submitted")
        val dateSubmitted: ZonedDateTime,

        @Column(name = "rating")
        val rating: Int
) : BaseEntity<Long>() {
    fun getAuthor(): User = rideRequest.requester
    fun getDriver(): User = rideRequest.ride.driver

    override fun toString(): String = "Rating id:[${getId()}], Ride Request id:[${rideRequest.id}], rating: [$rating], date: [$dateSubmitted]"

}