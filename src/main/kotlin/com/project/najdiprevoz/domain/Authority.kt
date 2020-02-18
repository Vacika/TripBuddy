package com.project.najdiprevoz.domain

import javax.persistence.*


@Entity
@Table(name = "authorities")
data class Authority(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        @Column(name = "id", nullable = false, unique = true)
        val id: Long? = null,

        @Column(name = "authority", nullable = false, unique = true)
        val authority: String? = null,
        // Inverse
        @OneToMany(mappedBy = "authority")
        val users: List<User> = ArrayList()
)