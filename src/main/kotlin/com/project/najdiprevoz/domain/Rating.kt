package com.project.najdiprevoz.domain

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
        private val rating: Int,
        private val date: Date) : BaseEntity<Long>()