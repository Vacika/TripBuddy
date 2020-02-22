package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.RideRequest
import com.project.najdiprevoz.enums.RequestStatus
import com.project.najdiprevoz.enums.RideStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CronJobService(private val rideRequestService: RideRequestService,
                     private val tripService: TripService) {

    val logger: Logger = LoggerFactory.getLogger(CronJobService::class.java)
//
//    @Scheduled(cron = "0 0/2 * * * *")
//    @Modifying
//    @Transactional
//    fun updateRidesAndRequestsJob() {
//        updateRideCron()
//        logger.info("[CRONJOB] Updating EXPIRED and RIDE_CANCELLED ride requests..")
//        updateRideRequestCron()
//    }


    private fun updateRideRequestCron() {
        rideRequestService.getAll()
                .filter { it.status == RequestStatus.PENDING && it.ride.status == RideStatus.FINISHED }
                .forEach { changeStatusByRideRequest(it, RequestStatus.EXPIRED) }
    }

    private fun changeStatusByRideRequest(rideRequest: RideRequest, status: RequestStatus) =
            rideRequestService.rideRequestCronJob(rideRequest, status)

    private fun updateRideCron() =
            tripService.checkForFinishedRidesTask()
}