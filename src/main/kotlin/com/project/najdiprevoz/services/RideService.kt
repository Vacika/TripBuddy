package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.*
import com.project.najdiprevoz.repositories.RideRepository
import com.project.najdiprevoz.web.request.create.CreateRideRequest
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class RideService(private val repository: RideRepository,
                  private val memberService: MemberService) {

    fun createNewRide(createRideRequest: CreateRideRequest) = repository.save(mapToRide(createRideRequest = createRideRequest))

    fun getPastRidesForMember(memberId: Long) = repository.findAllByDriver_IdAndFinishedIsTrue(driverId = memberId)

    fun setRideFinished(rideId: Long): Boolean = repository.setRideToFinished(rideId = rideId) == 1

    fun changeTimeDeparture(rideId: Long, newTime: ZonedDateTime) = repository.changeRideTiming(rideId = rideId, newTime = newTime) == 1

    fun deleteRide(rideId: Long) = repository.deleteById(rideId)

    fun findById(id: Long) = repository.findById(id)

    fun getAllRidesFromLocation(location: City) = repository.findAllByFromLocation(fromLocation = location)

    fun getAllRidesForDestination(destination: City) = repository.findAllByDestination(destination = destination)

    fun findAvailableSeatsForRide(rideId: Long) = repository.getAvailableSeatsForRide(rideId = rideId)

    fun findAllRidesForUser(user: Member) = repository.findAllByDriver(driver = user)

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
                rating = listOf<Rating>(),
                rideRequest = listOf<RideRequest>()
        )
    }
}
