package com.project.najdiprevoz.domain

import javax.persistence.*

@Deprecated("Remove this")
@Entity
@Table(name = "cars")
data class Car(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0L,

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
        val owner: User
) {
    override fun toString(): String {
        return "Brand: $brand, Model: $model, Year: $yearOfManufacture, totalSeats: $seats, owner: $owner "
    }

}