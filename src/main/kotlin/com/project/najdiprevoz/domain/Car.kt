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

        @ManyToOne(optional = true, fetch = FetchType.LAZY)
        @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false)
        val owner: Member
) : BaseEntity<Long>() {

//    override fun toString(): String {
//        return ""
////        return "Brand: $brand, Model: $model, Year: $yearOfManufacture, totalSeats: $seats, owner: $owner "
//    }
}