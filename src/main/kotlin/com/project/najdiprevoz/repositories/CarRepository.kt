package com.project.najdiprevoz.repositories

import com.project.najdiprevoz.domain.Car
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CarRepository : JpaRepository<Car, Long>