package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.RideRequest
import com.project.najdiprevoz.enums.RideRequestStatus
import com.project.najdiprevoz.enums.RideStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.repository.Modifying
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class CronJobService(private val rideRequestService: RideRequestService,
                     private val tripService: TripService) {

    val logger: Logger = LoggerFactory.getLogger(CronJobService::class.java)

    @Scheduled(cron = "0 0/20 * * * *")
    @Modifying
    @Transactional
    fun updateRidesAndRequestsJob() {
        updateRideCron()
        logger.info("[CRONJOB] Updating EXPIRED and RIDE_CANCELLED ride requests..")
        updateRideRequestCron()
    }

    private fun updateRideRequestCron() {
        rideRequestService.getAll()
                .filter { it.status == RideRequestStatus.PENDING && it.ride.status == RideStatus.FINISHED }
                .forEach { changeRequestToExpired(it) }
    }

    private fun changeRequestToExpired(rideRequest: RideRequest) =
            rideRequestService.rideRequestCronJob(rideRequest)

    private fun updateRideCron() =
            tripService.checkForFinishedTripsCronJob()
}