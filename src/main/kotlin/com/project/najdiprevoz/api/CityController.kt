package com.project.najdiprevoz.api

import com.project.najdiprevoz.services.CityService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/cities")
class CityController(private val service: CityService) {

    @GetMapping
    fun getAllCities() =
            service.findAll()

    @GetMapping("/add")
    fun addCity(name: String) =
            service.addCity(name)
}