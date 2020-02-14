package com.project.najdiprevoz.repositories

import com.project.najdiprevoz.domain.City
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CityRepository : JpaRepository<City, Long> {
    fun findByName(name: String): City
}