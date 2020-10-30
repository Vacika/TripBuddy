package com.project.najdiprevoz.domain

import org.springframework.data.annotation.Immutable
import java.time.ZonedDateTime
import javax.persistence.*

//TODO: Remove
@Entity
@Immutable
@Table(name = "v_ratings")
data class RatingView(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        val id: Long,

        @Column(name = "rating_id")
        val ratingId: Long,

        @Column(name = "rating")
        val rating: Int,

        @Column(name = "date_submitted")
        val dateSubmitted: ZonedDateTime,

        @Column(name = "reservation_request_id")
        val reservationRequestId: Long,

        @Column(name = "trip_id")
        val rideId: Long,

        @Column(name = "author_id")
        val authorId: Long,

        @Column(name = "driver_id")
        val driverId: Long
)