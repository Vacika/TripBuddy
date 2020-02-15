package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.Member
import com.project.najdiprevoz.domain.Rating
import com.project.najdiprevoz.domain.Ride
import com.project.najdiprevoz.domain.RideRequest
import com.project.najdiprevoz.exceptions.NotEnoughSeatsToDeleteException
import com.project.najdiprevoz.exceptions.RideNotFoundException
import com.project.najdiprevoz.repositories.RideRepository
import com.project.najdiprevoz.web.request.create.CreateRideRequest
import com.project.najdiprevoz.web.request.edit.EditRideRequest
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class RideService(private val repository: RideRepository,
                  private val memberService: MemberService,
                  private val cityService: CityService) {

    fun findAllActiveRides() =
            repository.findRidesByFinishedIsFalse()

    fun findAllActiveRidesWithAvailableSeats() =
            findAllActiveRides().filter { it.getAvailableSeats() > 0 }

    fun createNewRide(createRideRequest: CreateRideRequest) =
            repository.save(createRide(createRideRequest = createRideRequest))

    fun getPastRidesForMember(memberId: Long) =
            repository.findAllByDriver_IdAndFinishedIsTrue(driverId = memberId)

    fun setRideFinished(rideId: Long): Boolean =
            repository.setRideToFinished(rideId = rideId) == 1

//    fun changeTimeDeparture(rideId: Long, newTime: ZonedDateTime) =
//            repository.changeRideTiming(rideId = rideId, newTime = newTime) == 1

    fun deleteRide(rideId: Long) =
            repository.deleteById(rideId)

    fun findById(id: Long): Ride =
            repository.findById(id).orElseThrow { RideNotFoundException("Ride with id $id was not found") }

//    fun getAllRidesFromLocation(location: City) =
//            repository.findAllByFromLocation(fromLocation = location)
//
//    fun getAllRidesForDestination(destination: City) =
//            repository.findAllByDestination(destination = destination)

    fun findAvailableSeatsForRide(rideId: Long) =
            repository.getAvailableSeatsForRide(rideId = rideId)

    fun findAllRidesForUser(user: Member) =
            repository.findAllByDriver(driver = user)

    private fun createRide(createRideRequest: CreateRideRequest) = with(createRideRequest) {
        Ride(
                createdOn = ZonedDateTime.now(),
                fromLocation = cityService.findByName(fromLocation),
                destination = cityService.findByName(destination),
                departureTime = departureTime,
                totalSeatsOffered = totalSeats,
                finished = false,
                driver = memberService.findMemberById(driverId),
                pricePerHead = pricePerHead,
                additionalDescription = additionalDescription,
                rating = listOf<Rating>(),
                rideRequest = listOf<RideRequest>()
        )
    }

    fun findFromToRides(from: String, to: String): List<Ride> =
            repository.findAllByByFromLocation_NameAndDestination_Name(from, to)
                    .filter { !it.finished && it.getAvailableSeats() > 0 }

    fun editRide(rideId: Long, editRideRequest: EditRideRequest) = with(editRideRequest) {
        val ride = findById(rideId)
        ride.copy(fromLocation = cityService.findByName(fromLocation),
                departureTime = departureTime,
                destination = cityService.findByName(toLocation),
                additionalDescription = description,
                pricePerHead = pricePerHead)
        repository.save(ride)
    }

    fun decreaseSeatsOffered(rideId: Long, seatsToMinus: Int): Ride {
        val ride = findById(rideId)
        if (ride.getAvailableSeats() - seatsToMinus >= 0)
            ride.copy(totalSeatsOffered = ride.totalSeatsOffered - seatsToMinus)
        else throw NotEnoughSeatsToDeleteException(rideId, seatsToMinus, ride.getAvailableSeats())
        return ride
    }
}
