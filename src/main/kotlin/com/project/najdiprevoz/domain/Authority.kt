package com.project.najdiprevoz.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*


@Entity
@Table(name = "authorities")
data class Authority(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false, unique = true)
        val id: Long = 0L,

        @Column(name = "authority", nullable = false, unique = true)
        var authority: String? = null,
        // Inverse
        @JsonIgnore
        @OneToMany(mappedBy = "authority")
        var users: List<User> = ArrayList()
)