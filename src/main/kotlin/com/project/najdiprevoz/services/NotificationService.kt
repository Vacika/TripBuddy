package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.Notification
import com.project.najdiprevoz.domain.Rating
import com.project.najdiprevoz.domain.RideRequest
import com.project.najdiprevoz.domain.User
import com.project.najdiprevoz.enums.NotificationActions
import com.project.najdiprevoz.enums.NotificationType
import com.project.najdiprevoz.enums.RequestStatus
import com.project.najdiprevoz.exceptions.NotificationNotFoundException
import com.project.najdiprevoz.repositories.NotificationRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import javax.annotation.PostConstruct
import javax.transaction.Transactional

@Service
class NotificationService(private val repository: NotificationRepository) {

    val logger: Logger = LoggerFactory.getLogger(NotificationService::class.java)

    fun findById(id: Long) = repository.findById(id).orElseThrow { NotificationNotFoundException(id) }

    fun pushRequestStatusChangeNotification(rideRequest: RideRequest) {
        var notificationActionsAllowed: List<NotificationActions> = listOf(NotificationActions.MARK_AS_SEEN)
        var to: User
        var from: User
        val driver: User = rideRequest.ride.driver
        val requester: User = rideRequest.requester
        var notificationType: NotificationType
        when (rideRequest.status) {
            RequestStatus.APPROVED -> {
                notificationActionsAllowed.plus(NotificationActions.CANCEL)
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
                notificationActionsAllowed.plus(NotificationActions.DENY).plus(NotificationActions.APPROVE)
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
        pushNotification(from = from, to = to, rideRequest = rideRequest, type = notificationType, notificationActionsAllowed = notificationActionsAllowed)
        logger.info("[NOTIFICATIONS] Saving new notification for RideRequest[${rideRequest.id}], Notification Type:[${notificationType.name}]")
    }

    fun pushRatingNotification(rating: Rating) = with(rating) {
        pushNotification(
                from = rating.getAuthor(),
                to = rating.getDriver(),
                type = NotificationType.RATING_SUBMITTED,
                rideRequest = rating.rideRequest,
                notificationActionsAllowed = listOf(NotificationActions.MARK_AS_SEEN))

        logger.info("[NOTIFICATIONS] Saving new notification for RideRequest[${rideRequest.id}], Notification Type: [RATING_SUBMITTED]")
    }

    fun getMyNotifications(username: String) = repository.findAllByToUsername(username)

    fun getUnreadNotifications(username: String) = repository.findAllByToUsername(username).filter { !it.seen }

    private fun pushNotification(from: User, to: User, notificationActionsAllowed: List<NotificationActions>, type: NotificationType, rideRequest: RideRequest) {
        repository.saveAndFlush(Notification(
                from = from,
                to = to,
                actions = notificationActionsAllowed,
                type = type,
                createdOn = ZonedDateTime.now(),
                seen = false,
                rideRequest = rideRequest
        ))
    }

    fun markAsSeen(notificationId: Long) {
        repository.save(findById(notificationId).markAsSeen())
    }

    @Transactional
    @Modifying
    fun updateNotification(notification: Notification){
        repository.save(notification)
    }

//    @PostConstruct
//    fun testMarkAsSeen() {
//        markAsSeen(1)
//    }
}