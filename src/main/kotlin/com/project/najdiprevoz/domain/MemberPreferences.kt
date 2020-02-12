package com.project.najdiprevoz.domain

import javax.persistence.*

@Entity
@Table(name = "member_preferences")
data class MemberPreferences(
        @OneToOne
        @JoinColumn(name = "member_id", referencedColumnName = "id")
        private val member: Member,

        @Column(name = "is_smoking_allowed")
        private val isSmokingAllowed: Boolean? = false,

        @Column(name = "is_pet_allowed")
        private val isPetAllowed: Boolean? = false


) : BaseEntity<Long>()