package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.RideRequest
import com.project.najdiprevoz.enums.RequestStatus
import com.project.najdiprevoz.enums.RideStatus
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class CronJobService(private val rideRequestService: RideRequestService,
                     private val rideService: RideService) {

    @Scheduled(cron = "0 0/1 * * * *")
    private fun updateRidesAndRequestsJob() {
        updateRideCron()
        updateRideRequestCron()
    }

    private fun updateRideRequestCron() {
        val activeRequests = rideRequestService.getAll().filter { it.status == RequestStatus.PENDING }

        activeRequests
                .filter { it.ride.status == RideStatus.FINISHED }                    //RIDE FINISHED: Request Status ==> DENIED
                .forEach { changeStatusByRideRequest(it, RequestStatus.EXPIRED) }

        activeRequests
                .filter { it.ride.status == RideStatus.CANCELLED }
                .forEach { changeStatusByRideRequest(it, RequestStatus.RIDE_CANCELLED) }      //RIDE CANCELLED: Request Status ==> RIDE_CANCELLED
    }

    private fun changeStatusByRideRequest(rideRequest: RideRequest, status: RequestStatus) =
            rideRequestService.changeStatusByRideRequest(rideRequest, status)

    private fun updateRideCron() =
            rideService.checkForFinishedRidesTask()
}