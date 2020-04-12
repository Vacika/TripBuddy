package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.Notification
import com.project.najdiprevoz.domain.Rating
import com.project.najdiprevoz.domain.RideRequest
import com.project.najdiprevoz.domain.AppUser
import com.project.najdiprevoz.enums.NotificationAction
import com.project.najdiprevoz.enums.NotificationType
import com.project.najdiprevoz.enums.RequestStatus
import com.project.najdiprevoz.exceptions.NotificationNotFoundException
import com.project.najdiprevoz.repositories.NotificationRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class NotificationService(private val repository: NotificationRepository) {

    val logger: Logger = LoggerFactory.getLogger(NotificationService::class.java)

    fun findById(id: Long) = repository.findById(id).orElseThrow { NotificationNotFoundException(id) }

    @Modifying
    fun pushRequestStatusChangeNotification(rideRequest: RideRequest, notificationType: NotificationType) {
        var notificationActionAllowed: List<NotificationAction> = listOf(NotificationAction.MARK_AS_SEEN)
        var to: AppUser
        var from: AppUser
        val driver: AppUser = rideRequest.ride.driver
        val requester: AppUser = rideRequest.requester
//        var notificationType: NotificationType
        when (rideRequest.status) {
            RequestStatus.APPROVED -> {
                notificationActionAllowed = notificationActionAllowed.plus(NotificationAction.CANCEL)
                from = driver
                to = requester
//                notificationType = NotificationType.REQUEST_APPROVED
            }
            RequestStatus.DENIED -> {
                from = driver
                to = requester
//                notificationType = NotificationType.REQUEST_DENIED
            }
            RequestStatus.PENDING -> {
                notificationActionAllowed = notificationActionAllowed.plus(NotificationAction.DENY).plus(NotificationAction.APPROVE)
                from = requester
                to = driver
//                notificationType = NotificationType.REQUEST_SENT
            }
            RequestStatus.CANCELLED -> {
                from = requester
                to = driver
//                notificationType = NotificationType.REQUEST_CANCELLED
            }
            RequestStatus.RIDE_CANCELLED -> {
                from = driver
                to = requester
//                notificationType = NotificationType.RIDE_CANCELLED
            }
            RequestStatus.EXPIRED -> {
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

    private fun pushNotification(from: AppUser, to: AppUser, notificationActionAllowed: List<NotificationAction>, type: NotificationType, rideRequest: RideRequest) {
        repository.saveAndFlush(Notification(
                from = from,
                to = to,
                actions = notificationActionAllowed,
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
        notification.actions = listOf()
        repository.save(notification)
    }
}