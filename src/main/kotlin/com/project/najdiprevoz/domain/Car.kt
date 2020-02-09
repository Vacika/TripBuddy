package com.project.najdiprevoz.domain

import javax.persistence.*

@Entity
@Table(name = "cars")
data class Car (
        @Column(name="brand")
        private val brand: String,

        @Column(name="model")
        private val model: String,

        @Column(name="year_manufacture")
        private val yearOfManufacture: Int,

        @Column(name="total_seats")
        private val seats: Int,

        @OneToOne
        @JoinColumn(name="car_id", referencedColumnName = "id")
        private val owner: Member
) : BaseEntity<Long>()