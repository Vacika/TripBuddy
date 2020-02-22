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

//    @Scheduled(cron = "0 0/3 * * * *")
//    @Modifying
//    @Transactional
//    fun updateRidesAndRequestsJob() {
//        updateRideCron()
//        logger.info("[CRONJOB] Updating EXPIRED and RIDE_CANCELLED ride requests..")
//        updateRideRequestCron()
//    }


    private fun updateRideRequestCron() {
        val activeRequests = rideRequestService.getAll().filter { it.status == RequestStatus.PENDING }

        //RIDE FINISHED ===>  CHANGE ALL ACTIVE REQUESTS STATUS TO EXPIRED
        activeRequests
                .filter { it.ride.status == RideStatus.FINISHED }
                .forEach { changeStatusByRideRequest(it, RequestStatus.EXPIRED) }

        //RIDE CANCELLED ===> CHANGE ALL ACTIVE REQUESTS STATUS TO RIDE_CANCELLED
        activeRequests
                .filter { it.ride.status == RideStatus.CANCELLED }
                .forEach { changeStatusByRideRequest(it, RequestStatus.RIDE_CANCELLED) }
    }

    private fun changeStatusByRideRequest(rideRequest: RideRequest, status: RequestStatus) =
            rideRequestService.changeStatusByRideRequest(rideRequest, status)

    private fun updateRideCron() =
            tripService.checkForFinishedRidesTask()
}