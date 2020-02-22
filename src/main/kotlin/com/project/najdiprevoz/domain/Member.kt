package com.project.najdiprevoz.domain

import com.project.najdiprevoz.enums.Gender
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "members")
data class Member(
        @Column(name = "email", nullable = false)
        val email: String,

        @Column(name = "first_name", nullable = false)
        val firstName: String,

        @Column(name = "last_name", nullable = false)
        val lastName: String,

        @Column(name = "birth_date", nullable = false)
        val birthDate: Date,

        @Column(name = "profile_photo", nullable = true)
        val profilePhoto: String?,

        @Column(name = "password", nullable = false)
        val password: String,

        @Column(name = "gender", nullable = false)
        @Enumerated(EnumType.STRING)
        val gender: Gender,

        @Column(name = "phone_number", nullable = false)
        val phoneNumber: String
) : BaseEntity<Long>()