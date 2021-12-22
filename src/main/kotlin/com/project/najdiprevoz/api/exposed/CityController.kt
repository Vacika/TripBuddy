package com.project.najdiprevoz.api.exposed

import com.project.najdiprevoz.services.CityService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/public/cities")
class CityController(private val service: CityService) {

    @GetMapping
    fun getAllCities() =
            service.findAll()

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/add")
    fun addCity(name: String) =
            service.addCity(name)
}