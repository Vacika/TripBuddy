package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.Car
import com.project.najdiprevoz.exceptions.NoCarFoundForUserException
import com.project.najdiprevoz.repositories.CarRepository
import com.project.najdiprevoz.web.request.create.CreateCarRequest
import org.springframework.stereotype.Service

@Service
class CarService(private val repository: CarRepository,
                 private val memberService: MemberService) {

    fun addNewCar(addCarRequest: CreateCarRequest) =
            repository.save(mapToCar(addCarRequest))


    fun removeCar(car: Car) =
            repository.delete(car);

    fun removeCarByMemberId(memberId: Long) =
            repository.deleteCarByOwner_Id(memberId)

    fun findCarForMember(memberId: Long): Car? {
        return repository.findByOwner_Id(ownerId = memberId)
                .orElseThrow { NoCarFoundForUserException(memberId) }
    }

    private fun mapToCar(addCarRequest: CreateCarRequest) =
            with(addCarRequest) {
                Car(
                        brand = brand,
                        model = model,
                        owner = memberService.findMemberById(ownerId),
                        yearOfManufacture = yearManufacture,
                        seats = totalSeats)
            }
}