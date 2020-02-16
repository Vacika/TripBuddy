package com.project.najdiprevoz.domain

import com.fasterxml.jackson.annotation.JsonManagedReference
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
        val author: Member,

        @JsonManagedReference
        @ManyToOne
        @JoinColumn(name = "ride_id", referencedColumnName = "id", nullable = false)
        val ride: Ride,

        val note: String?,

        val dateSubmitted: ZonedDateTime,

        val rating: Int
) : BaseEntity<Long>()