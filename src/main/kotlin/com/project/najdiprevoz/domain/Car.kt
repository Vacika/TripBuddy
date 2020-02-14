package com.project.najdiprevoz.domain

import javax.persistence.*

@Entity
@Table(name = "cars")
data class Car(
        @Column(name = "brand")
        val brand: String,

        @Column(name = "model")
        val model: String,

        @Column(name = "year_manufacture")
        val yearOfManufacture: Int,

        @Column(name = "total_seats")
        val seats: Int,

        @OneToOne
        @JoinColumn(name = "owner_id", referencedColumnName = "id")
        val owner: Member
) : BaseEntity<Long>()