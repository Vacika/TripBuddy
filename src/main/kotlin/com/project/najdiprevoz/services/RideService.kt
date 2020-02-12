package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.Ride
import com.project.najdiprevoz.repositories.RideRepository
import com.project.najdiprevoz.web.request.CreateRideRequest
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class RideService(private val repository: RideRepository,
                  private val memberService: MemberService) {

    fun createNewRide(createRideRequest: CreateRideRequest): Ride {
        return repository.save(mapToRide(createRideRequest))
    }

    private fun mapToRide(createRideRequest: CreateRideRequest) = with(createRideRequest) {
        Ride(
                createdOn = ZonedDateTime.now(),
                fromLocation = fromLocation,
                destination = destination,
                departureTime = departureTime,
                totalSeats = totalSeats,
                finished = false,
                driver = memberService.findMemberById(driverId),
                pricePerHead = pricePerHead,
                additionalDescription = additionalDescription,
                rating = listOf(),
                rideRequest = listOf()
        )
    }

    fun getPastRidesForMember(memberId: Long): List<Ride>? {
        return repository.findAllByDriver_IdAndFinishedIsTrue(memberId)
    }

    fun findById(id: Long) = repository.findById(id)
}