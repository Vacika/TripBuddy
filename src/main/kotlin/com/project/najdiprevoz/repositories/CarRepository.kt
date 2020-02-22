package com.project.najdiprevoz.repositories

import com.project.najdiprevoz.domain.Car
import com.project.najdiprevoz.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CarRepository : JpaRepository<Car, Long> {

    fun findByOwnerId(ownerId: Long): Optional<Car>
    fun findByOwner(owner: User): Car?
    fun deleteCarByOwnerId(ownerId: Long)
}