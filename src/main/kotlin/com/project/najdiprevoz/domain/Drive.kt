package com.project.najdiprevoz.domain

import java.time.ZonedDateTime
import javax.persistence.*

@Entity
data class Drive (
        @Column(name="from_location")
        private val fromLocation: String,

        @Column(name="destination")
        private val destination: String,

        @Column(name="departure_time")
        private val departureTime: ZonedDateTime,

        @Column(name="total_seats")
        private val totalSeats: Int,

        @Column(name="is_finished")
        private val finished: Boolean,

        @ManyToOne(fetch= FetchType.LAZY,optional = false)
        @JoinColumn(name="driver_id",nullable = false)
        private val driver: Member


) : BaseEntity<Long>()