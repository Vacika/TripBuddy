package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.Notification
import com.project.najdiprevoz.domain.Rating
import com.project.najdiprevoz.domain.RideRequest
import com.project.najdiprevoz.domain.User
import com.project.najdiprevoz.enums.NotificationAction
import com.project.najdiprevoz.enums.NotificationType
import com.project.najdiprevoz.enums.RideRequestStatus
import com.project.najdiprevoz.exceptions.NotificationNotFoundException
import com.project.najdiprevoz.repositories.NotificationRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import javax.transaction.Transactional

@Service
class NotificationService(private val repository: NotificationRepository) {

    val logger: Logger = LoggerFactory.getLogger(NotificationService::class.java)

    fun findById(id: Long) = repository.findById(id).orElseThrow { NotificationNotFoundException(id) }

    //TODO: Remove all previous notifications with same requestId when pushing new notification (if it is not first)
    @Modifying
    fun pushRequestStatusChangeNotification(rideRequest: RideRequest, notificationType: NotificationType) {
        var notificationActionAllowed: List<NotificationAction> = listOf(NotificationAction.MARK_AS_SEEN)
        var to: User
        var from: User
        val driver: User = rideRequest.ride.driver
        val requester: User = rideRequest.requester
//        var notificationType: NotificationType
        when (rideRequest.status) {
            RideRequestStatus.APPROVED -> {
                notificationActionAllowed = notificationActionAllowed.plus(NotificationAction.CANCEL)
                from = driver
                to = requester
//                notificationType = NotificationType.REQUEST_APPROVED
            }
            RideRequestStatus.DENIED -> {
                from = driver
                to = requester
//                notificationType = NotificationType.REQUEST_DENIED
            }
            RideRequestStatus.PENDING -> {
                notificationActionAllowed = notificationActionAllowed.plus(NotificationAction.DENY).plus(NotificationAction.APPROVE)
                from = requester
                to = driver
//                notificationType = NotificationType.REQUEST_SENT
            }
            RideRequestStatus.CANCELLED -> {
                from = requester
                to = driver
//                notificationType = NotificationType.REQUEST_CANCELLED
            }
            RideRequestStatus.RIDE_CANCELLED -> {
                from = driver
                to = requester
//                notificationType = NotificationType.RIDE_CANCELLED
            }
            RideRequestStatus.EXPIRED -> {
                from = driver
                to = requester
//                notificationType = NotificationType.REQUEST_EXPIRED
            }
        }
        pushNotification(from = from, to = to, rideRequest = rideRequest, type = notificationType, notificationActionAllowed = notificationActionAllowed)
        logger.info("[NOTIFICATIONS] Saving new notification for RideRequest[${rideRequest.id}], Notification Type:[${notificationType.name}]")
    }

    @Modifying
    fun pushRatingNotification(rating: Rating) = with(rating) {
        pushNotification(
                from = rating.getAuthor(),
                to = rating.getDriver(),
                type = NotificationType.RATING_SUBMITTED,
                rideRequest = rating.rideRequest,
                notificationActionAllowed = listOf(NotificationAction.MARK_AS_SEEN))

        logger.info("[NOTIFICATIONS] Saving new notification for RideRequest[${rideRequest.id}], Notification Type: [RATING_SUBMITTED]")
    }

    fun getMyNotifications(username: String) = repository.findAllByToUsernameOrderByCreatedOnDesc(username)

    fun getUnreadNotifications(username: String) = repository.findAllByToUsernameAndSeenIsFalseOrderByCreatedOnDesc(username)

    private fun pushNotification(from: User, to: User, notificationActionAllowed: List<NotificationAction>, type: NotificationType, rideRequest: RideRequest) {
        repository.saveAndFlush(Notification(
                from = from,
                to = to,
                actions = notificationActionAllowed.toMutableList(),
                type = type,
                createdOn = ZonedDateTime.now(),
                seen = false,
                rideRequest = rideRequest
        ))
    }

    @Modifying
    fun markAsSeen(notificationId: Long) {
        repository.save(findById(notificationId).markAsSeen())
    }

    @Modifying
    fun removeAllNotificationsForRideRequest(requestId: Long) {
        repository.findByRideRequestId(requestId).forEach { repository.delete(it) }
    }

    fun removeAllActionsForNotification(notificationId: Long) {
        val notification = findById(notificationId)
        notification.actions = mutableListOf()
        repository.save(notification)
    }

    @Modifying
    fun removeLastNotificationForRideRequest(requestId: Long) {
        val notification = repository.findByRideRequestId(requestId).maxBy { it.createdOn }!!
        repository.delete(notification)
        repository.flush()
    }
}