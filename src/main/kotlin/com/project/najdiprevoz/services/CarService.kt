package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.Car
import com.project.najdiprevoz.exceptions.CarNotFoundException
import com.project.najdiprevoz.exceptions.NoCarFoundForUserException
import com.project.najdiprevoz.repositories.CarRepository
import com.project.najdiprevoz.web.request.create.CreateCarRequest
import com.project.najdiprevoz.web.request.edit.EditCarRequest
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct
import javax.transaction.Transactional

@Service
class CarService(private val repository: CarRepository,
                 private val memberService: MemberService) {

    @Transactional
    fun addNewCar(addCarRequest: CreateCarRequest) =
            repository.save(mapToCar(addCarRequest))


    fun removeCar(carId: Long) =
            repository.deleteById(carId)

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

    @PostConstruct
    fun test() {
        val res1 = addNewCar(CreateCarRequest(
                brand = "Zastava",
                model = "101",
                yearManufacture = 1992,
                ownerId = 1,
                totalSeats = 3
        ))
    }

    fun findById(carId: Long): Car = repository.findById(carId).orElseThrow { CarNotFoundException(carId) }

    @Modifying
    fun editCar(carId: Long, editCarRequest: EditCarRequest) = with(editCarRequest) {
        val member = findById(carId)
        member.copy(brand = brand,
                model = model,
                yearOfManufacture = yearManufacture,
                seats = seats)
        repository.save(member)
    }
}