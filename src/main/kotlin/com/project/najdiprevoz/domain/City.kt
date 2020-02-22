package com.project.najdiprevoz.domain

import javax.persistence.*

@Entity
@Table(name = "cities")
data class City(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0L,

        @Column(name = "name")
        val name: String
)