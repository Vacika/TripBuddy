package com.project.najdiprevoz.domain

import java.time.ZonedDateTime
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "ratings")
data class Rating(
        @ManyToOne
        @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false)
        private val author: Member,

        @ManyToOne
        @JoinColumn(name = "ride_id", referencedColumnName = "id", nullable = false)
        private val ride: Ride,

        private val note: String?,

        private val dateSubmitted: ZonedDateTime
) : BaseEntity<Long>()