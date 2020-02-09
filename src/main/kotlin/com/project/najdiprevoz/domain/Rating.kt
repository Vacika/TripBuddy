package com.project.najdiprevoz.domain

import java.time.ZonedDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "ratings")
data class Rating(
        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "member_from_id", nullable = false)
        private val from: Member,

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "member_to_id", nullable = false)
        private val to: Member,

        @Column(name = "rating")
        private val rating: Int,

        @Column(name = "date_submitted")
        private val date: ZonedDateTime) : BaseEntity<Long>()