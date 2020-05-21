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

@Service
class NotificationService(private val repository: NotificationRepository) {

    val logger: Logger = LoggerFactory.getLogger(NotificationService::class.java)

    fun findById(id: Long) = repository.findById(id).orElseThrow { NotificationNotFoundException(id) }

    //TODO: Remove all previous/last notifications with same requestId when pushing new notification (if it is not first)
    @Modifying
    fun pushRequestStatusChangeNotification(rideRequest: RideRequest, notificationType: NotificationType) {
        var notificationActionAllowed: List<NotificationAction> = listOf(NotificationAction.MARK_AS_SEEN)
        var to: User
        var from: User
        val driver: User = rideRequest.ride.driver
        val requester: User = rideRequest.requester
        when (rideRequest.status) {
            RideRequestStatus.APPROVED -> {
                notificationActionAllowed = notificationActionAllowed.plus(NotificationAction.CANCEL)
                from = driver
                to = requester
            }
            RideRequestStatus.DENIED -> {
                from = driver
                to = requester
            }
            RideRequestStatus.PENDING -> {
                notificationActionAllowed = notificationActionAllowed.plus(NotificationAction.DENY).plus(
                        NotificationAction.APPROVE)
                from = requester
                to = driver
            }
            RideRequestStatus.CANCELLED -> {
                from = requester
                to = driver
            }
            RideRequestStatus.RIDE_CANCELLED -> {
                from = driver
                to = requester
            }
            RideRequestStatus.EXPIRED -> {
                from = driver
                to = requester
            }
        }
        removeLastNotificationForRideRequest(rideRequest.id)
        pushNotification(from = from, to = to, rideRequest = rideRequest, type = notificationType,
                         notificationActionAllowed = notificationActionAllowed)
    }

    @Modifying
    fun pushRatingNotification(rating: Rating) = with(rating) {
        pushNotification(
                from = rating.getAuthor(),
                to = rating.getDriver(),
                type = NotificationType.RATING_SUBMITTED,
                rideRequest = rating.rideRequest,
                notificationActionAllowed = listOf(NotificationAction.MARK_AS_SEEN))
    }

    @Modifying
    fun pushRatingAllowedNotification(rideRequest: RideRequest) = with(rideRequest) {
        pushNotification(from = ride.driver,
                         to = requester,
                         notificationActionAllowed = listOf(NotificationAction.SUBMIT_RATING,
                                                            NotificationAction.MARK_AS_SEEN),
                         rideRequest = this,
                         type = NotificationType.RATING_ALLOWED)
    }

    fun getMyNotifications(username: String) = repository.findAllByToUsernameOrderByCreatedOnDesc(username)

    fun getUnreadNotifications(username: String) = repository.findAllByToUsernameAndSeenIsFalseOrderByCreatedOnDesc(
            username)

    private fun pushNotification(from: User, to: User, notificationActionAllowed: List<NotificationAction>,
                                 type: NotificationType, rideRequest: RideRequest) {
        logger.info("[NOTIFICATIONS] Saving new notification for RideRequest[${rideRequest.id}], Notification Type:[${type.name}]")
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
        logger.debug("[NOTIFICATIONS] Mark as SEEN notification ID [$notificationId]")
        repository.save(findById(notificationId).markAsSeen())
    }

    @Modifying
    fun removeAllNotificationsForRideRequest(requestId: Long) {
        logger.debug("[NOTIFICATIONS] Removing all notifications associated with RideRequest with ID: [$requestId]")
        repository.findByRideRequestId(requestId).forEach { repository.delete(it) }
    }

    fun removeAllActionsForNotification(notificationId: Long) {
        val notification = findById(notificationId)
        notification.actions = mutableListOf()
        logger.debug("[NOTIFICATIONS] Removing all actions associated with notification with ID: [$notificationId]")
        repository.save(notification)
    }

    @Modifying
    fun removeLastNotificationForRideRequest(requestId: Long) {
        val notification = repository.findByRideRequestId(requestId).maxBy { it.createdOn }
        if(notification!=null) {
            logger.debug("[NOTIFICATIONS] Removing last notification associated with RideRequest with ID: [$requestId]")
            repository.delete(notification)
            repository.flush()
        }
    }

    fun checkIfHasRatingAllowedNotification(rideRequest: RideRequest): Boolean =
            repository.findAllByTypeAndRideRequest(NotificationType.RATING_ALLOWED, rideRequest).isNotEmpty()

}