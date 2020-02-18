package com.project.najdiprevoz.api

import com.project.najdiprevoz.services.CarService
import com.project.najdiprevoz.web.request.edit.EditCarRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/cars")
class CarController(private val service: CarService) {

    @GetMapping("/member/{userId}")
    fun getCarForMember(@PathVariable("userId") userId: Long) =
            service.findCarForMember(userId)

    @GetMapping("/edit/{carId}")
    fun editCar(@PathVariable("carId") carId: Long, editCarRequest: EditCarRequest) =
            service.editCar(carId, editCarRequest)

    @GetMapping("/delete/{carId}")
    fun editCar(@PathVariable("carId") carId: Long) =
            service.removeCar(carId)

    @GetMapping("/{carId}")
    fun getCar(@PathVariable("carId") carId: Long) =
            service.findById(carId)
}