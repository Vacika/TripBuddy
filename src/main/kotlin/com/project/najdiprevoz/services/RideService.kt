package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.*
import com.project.najdiprevoz.enums.RideStatus
import com.project.najdiprevoz.exceptions.NotEnoughSeatsToDeleteException
import com.project.najdiprevoz.exceptions.RideNotFoundException
import com.project.najdiprevoz.repositories.RideRepository
import com.project.najdiprevoz.web.request.create.CreateRideRequest
import com.project.najdiprevoz.web.request.edit.EditRideRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import javax.annotation.PostConstruct

@Service
class RideService(private val repository: RideRepository,
                  private val memberService: MemberService,
                  private val cityService: CityService,
                  private val notificationService: NotificationService) {

    val logger: Logger = LoggerFactory.getLogger(RideService::class.java)

    fun findAllActiveRides(): List<Ride> =
            repository.findAllByStatus(RideStatus.ACTIVE)

    fun findAllActiveRidesWithAvailableSeats() =
            findAllActiveRides().filter { it.getAvailableSeats() > 0 }

    fun createNewRide(createRideRequest: CreateRideRequest) =
            repository.save(createRide(createRideRequest = createRideRequest))

    fun getPastRidesForMember(memberId: Long) =
            repository.findAllByDriverIdAndStatus(driverId = memberId, status = RideStatus.FINISHED)

    fun setRideFinished(rideId: Long): Boolean =
            repository.changeRideStatus(rideId = rideId, status = RideStatus.FINISHED) == 1


    fun deleteRide(rideId: Long) {
        val ride = findById(rideId)
        ride.rideRequests.forEach { pushNotification(it, NotificationType.RIDE_CANCELLED) }
        repository.changeRideStatus(rideId, RideStatus.CANCELLED)
    }

    private fun pushNotification(req: RideRequest, type: NotificationType) {
        notificationService.pushNotification(req, type)
    }

    fun findById(id: Long): Ride =
            repository.findById(id).orElseThrow { RideNotFoundException("Ride with id $id was not found") }

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
                driver = memberService.findMemberById(driverId),
                pricePerHead = pricePerHead,
                additionalDescription = additionalDescription,
                rating = listOf<Rating>(),
                rideRequests = listOf<RideRequest>(),
                status = RideStatus.ACTIVE
        )
    }

    fun findFromToRides(from: String, to: String): List<Ride> =
            repository.findAllByFromLocationNameAndDestinationName(from, to)
                    .filter { it.status == RideStatus.ACTIVE && it.getAvailableSeats() > 0 }

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
        if (ride.getAvailableSeats().minus(seatsToMinus) >= 0)
            ride.copy(totalSeatsOffered = ride.totalSeatsOffered - seatsToMinus)
        else throw NotEnoughSeatsToDeleteException(rideId, seatsToMinus, ride.getAvailableSeats())
        return ride
    }


    @Scheduled(cron = "0 0/1 * * * *")
     fun checkForFinishedRidesTask() {
        logger.info("[CRONJOB] Checking for finished rides..")
        logger.info("[CRONJOB] Updated [" + repository.updateRidesCron(ZonedDateTime.now()) + "] rides.")
        logger.info("[CRONJOB] Updating ride requests..")

    }

//    @PostConstruct
//    fun test() {
//        val t = deleteRide(2)
//    }

    //    fun getAllRidesFromLocation(location: City) =
//            repository.findAllByFromLocation(fromLocation = location)
//
//    fun getAllRidesForDestination(destination: City) =
//            repository.findAllByDestination(destination = destination)

}
