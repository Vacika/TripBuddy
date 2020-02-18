package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.*
import com.project.najdiprevoz.enums.RequestStatus
import com.project.najdiprevoz.repositories.NotificationRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class NotificationService(private val repository: NotificationRepository) {

    val logger: Logger = LoggerFactory.getLogger(NotificationService::class.java)


    private fun pushNotification(from: User, to: User, actionsAllowed: List<String>, type: NotificationType, rideRequest: RideRequest) {
        repository.saveAndFlush(Notification(
                from = from,
                to = to,
                actionsAvailable = actionsAllowed.joinToString(separator = ", "),
                type = type,
                createdOn = ZonedDateTime.now(),
                seen = false,
                rideRequest = rideRequest
        ))
    }

    fun pushRequestStatusChangeNotification(rideRequest: RideRequest) {
        var actionsAllowed: List<String> = listOf(Actions.MARK_AS_SEEN.name)
        var to: User
        var from: User
        val driver: User = rideRequest.ride.driver
        val requester: User = rideRequest.requester
        var notificationType: NotificationType

        when (rideRequest.status) {
            RequestStatus.APPROVED -> {
                actionsAllowed.plus(Actions.CANCEL.name)
                from = driver
                to = requester
                notificationType = NotificationType.REQUEST_APPROVED
            }
            RequestStatus.DENIED -> {
                from = driver
                to = requester
                notificationType = NotificationType.REQUEST_DENIED

            }
            RequestStatus.PENDING -> {
                actionsAllowed.plus(Actions.DENY.name).plus(Actions.APPROVE.name)
                from = requester
                to = driver
                notificationType = NotificationType.REQUEST_SENT
            }
            RequestStatus.CANCELLED -> {
                from = requester
                to = driver
                notificationType = NotificationType.REQUEST_CANCELLED
            }
            RequestStatus.RIDE_CANCELLED -> {
                from = driver
                to = requester
                notificationType = NotificationType.RIDE_CANCELLED
            }
            RequestStatus.EXPIRED -> {
                from = driver
                to = requester
                notificationType = NotificationType.REQUEST_EXPIRED
            }
        }

        pushNotification(from = from, to = to, rideRequest = rideRequest, type = notificationType, actionsAllowed = actionsAllowed)
        logger.info("[NOTIFICATIONS] Saving new notification for RideRequest[${rideRequest.id}], Notification Type:[${notificationType.name}]")
    }

    fun pushRequestStatusChangeNotification(rideRequest: RideRequest, type: NotificationType) {
        var status = when (type) {
            NotificationType.RIDE_CANCELLED -> RequestStatus.RIDE_CANCELLED
            NotificationType.REQUEST_SENT -> RequestStatus.PENDING
            NotificationType.REQUEST_CANCELLED -> RequestStatus.CANCELLED
            NotificationType.REQUEST_APPROVED -> RequestStatus.APPROVED
            NotificationType.REQUEST_DENIED -> RequestStatus.DENIED
            NotificationType.REQUEST_EXPIRED -> RequestStatus.EXPIRED
            NotificationType.RATING_SUBMITTED -> throw Exception("RATING_SUBMITTED notification type can not be used on ride request status change!")
        }
        rideRequest.status = status
        pushRequestStatusChangeNotification(rideRequest)
    }

    fun pushRatingNotification(rating: Rating) = with(rating) {
        pushNotification(
                from = rating.getAuthor(),
                to = rating.getDriver(),
                type = NotificationType.RATING_SUBMITTED,
                rideRequest = rating.rideRequest,
                actionsAllowed = listOf(Actions.MARK_AS_SEEN.name))

        logger.info("[NOTIFICATIONS] Saving new notification for RideRequest[${rideRequest.id}], Notification Type: [RATING_SUBMITTED]")
    }
}