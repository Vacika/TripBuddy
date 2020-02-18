package com.project.najdiprevoz.domain

import javax.persistence.*

@Entity
@Table(name = "member_preferences")
data class MemberPreferences(
        @OneToOne
        @JoinColumn(name = "member_id", referencedColumnName = "id")
        val user: User,

        @Column(name = "is_smoking_allowed")
        val isSmokingAllowed: Boolean? = false,

        @Column(name = "is_pet_allowed")
        val isPetAllowed: Boolean? = false


) : BaseEntity<Long>()