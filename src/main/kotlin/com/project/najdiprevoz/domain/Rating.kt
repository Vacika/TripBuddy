package com.project.najdiprevoz.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import java.time.ZonedDateTime
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "ratings")
data class Rating(
        @JsonBackReference
        @OneToOne
        @JoinColumn(name = "ride_request_id", referencedColumnName = "id", nullable = false)
        val rideRequest: RideRequest,

        val note: String?,

        val dateSubmitted: ZonedDateTime,

        val rating: Int
) : BaseEntity<Long>() {
    fun getAuthor(): Member = rideRequest.requester
    fun getDriver(): Member = rideRequest.ride.driver
}