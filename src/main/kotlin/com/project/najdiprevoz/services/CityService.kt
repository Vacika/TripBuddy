package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.City
import com.project.najdiprevoz.repositories.CityRepository
import javassist.NotFoundException
import org.springframework.stereotype.Service

@Service
class CityService(private val repository: CityRepository) {

    fun findByName(name: String) =
            repository.findByName(name)

    fun findAll(): List<City> =
            repository.findAll()

    fun addCity(name: String): City =
            repository.save(City(name = name))

    fun findById(id: Long): City =
        repository.findById(id).orElseThrow { NotFoundException("No city found with id $id") }
}