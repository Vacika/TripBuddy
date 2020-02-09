package com.project.najdiprevoz.domain

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "members")
data class Member(
        @Column(name = "email", nullable = false)
        private val email: String,

        @Column(name = "first_name", nullable = false)
        private val firstName: String,

        @Column(name = "last_name", nullable = false)
        private val lastName: String,

        @Column(name = "birth_date", nullable = false)
        private val birthDate: Date,

        @Column(name = "profile_photo", nullable = false)
        private val profilePhoto: String,

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "to")
        private val ratings: List<Rating>,

        @Column(name = "password", nullable = false)
        private val password: String,

        @Column(name = "gender", nullable = false)
        @Enumerated(EnumType.STRING)
        private val gender: Gender,

        @Column(name = "phone_number", nullable = false)
        private val phoneNumber: String,

        @OneToOne
        private val car: Car,

        @OneToMany(mappedBy = "requester")
        private val rideRequests: List<RideRequest>?
) : BaseEntity<Long>()