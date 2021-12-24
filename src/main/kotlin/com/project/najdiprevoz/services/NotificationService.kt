package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.Notification
import com.project.najdiprevoz.domain.Rating
import com.project.najdiprevoz.domain.ReservationRequest
import com.project.najdiprevoz.domain.User
import com.project.najdiprevoz.enums.NotificationAction
import com.project.najdiprevoz.enums.NotificationType
import com.project.najdiprevoz.exceptions.NotificationNotFoundException
import com.project.najdiprevoz.repositories.NotificationRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Service
import java.lang.RuntimeException
import java.time.ZonedDateTime
import javax.transaction.Transactional

@Service
class NotificationService(private val repository: NotificationRepository) {

    val logger: Logger = LoggerFactory.getLogger(NotificationService::class.java)

    fun findById(id: Long): Notification = repository.findById(id).orElseThrow { NotificationNotFoundException(id) }

    fun getMyNotifications(username: String) = repository.findAllByToUsernameOrderByCreatedOnDesc(username)

    fun pushReservationStatusChangeNotification(
        reservationRequest: ReservationRequest,
        notificationType: NotificationType
    ) {
        var notificationActionAllowed: List<NotificationAction> = listOf(NotificationAction.MARK_AS_SEEN)
        val to: User
        val from: User
        val driver: User = reservationRequest.trip.driver
        val requester: User = reservationRequest.requester
        when (notificationType) {
            NotificationType.REQUEST_APPROVED -> {
                notificationActionAllowed = notificationActionAllowed.plus(NotificationAction.CANCEL_RESERVATION)
                from = driver
                to = requester
            }
            NotificationType.REQUEST_DENIED -> {
                from = driver
                to = requester
            }
            NotificationType.REQUEST_SENT -> {
                notificationActionAllowed =
                    notificationActionAllowed.plus(NotificationAction.DENY_RESERVATION).plus(NotificationAction.APPROVE_RESERVATION)
                from = requester
                to = driver
            }
            NotificationType.REQUEST_CANCELLED -> {
                from = requester
                to = driver
            }
            NotificationType.RIDE_CANCELLED -> {
                from = driver
                to = requester
            }
            NotificationType.REQUEST_EXPIRED -> {
                from = driver
                to = requester
            }
            else -> throw RuntimeException("Something went wrong here. Notification type: ${notificationType.name}, ID: ${reservationRequest.id}")
        }
        removeOldNotificationsActions(reservationRequest.id)
        pushNotification(
            from = from, to = to, reservationRequest = reservationRequest, type = notificationType,
            notificationActionAllowed = notificationActionAllowed
        )
    }

    fun pushRatingSubmittedNotification(rating: Rating) = with(rating) {
        pushNotification(
            from = getAuthor(),
            to = getDriver(),
            type = NotificationType.RATING_SUBMITTED,
            reservationRequest = reservationRequest,
            notificationActionAllowed = listOf(NotificationAction.MARK_AS_SEEN)
        )
    }

    fun pushRatingAllowedNotification(reservationRequest: ReservationRequest) = with(reservationRequest) {
        pushNotification(
            from = trip.driver,
            to = requester,
            notificationActionAllowed = listOf(
                NotificationAction.SUBMIT_RATING,
                NotificationAction.MARK_AS_SEEN
            ),
            reservationRequest = this,
            type = NotificationType.RATING_ALLOWED
        )
    }

    private fun pushNotification(
        from: User, to: User, notificationActionAllowed: List<NotificationAction>,
        type: NotificationType, reservationRequest: ReservationRequest
    ) {
        logger.info(
            "[NOTIFICATIONS] Saving new notification for ReservationRequest[${reservationRequest.id}], Notification Type:[${type.name}]"
        )
        repository.saveAndFlush(
            Notification(
                from = from,
                to = to,
                actions = notificationActionAllowed.toMutableList(),
                type = type,
                createdOn = ZonedDateTime.now(),
                seen = false,
                reservationRequest = reservationRequest
            )
        )
    }

    @Modifying
    fun markAsSeen(notificationId: Long) {
        logger.debug("[NOTIFICATIONS] Mark as SEEN notification ID [$notificationId]")
        repository.save(findById(notificationId).markAsSeen())
    }

    @Transactional
    @Modifying
    fun removeOldNotificationsActions(requestId: Long) {
        repository.findAllByReservationRequest_Id(requestId).forEach { notification ->
            logger.debug(
                    "[NOTIFICATIONS] Removing ACTIONS for last notification associated with ReservationRequest with ID: [$requestId]")
            repository.save(notification.removeAllActions())
        }
    }


//    @Transactional
//    @Modifying
//    fun removeLastNotificationForReservation(requestId: Long, user: User) {
//        var notification = repository.findAllByReservationRequest_IdAndTo(requestId, user)
//        if (notification.isNotEmpty()) {
//            val deleteNotification = notification.maxBy { it.createdOn }!!
//            logger.debug("[NOTIFICATIONS] Removing last notification associated with ReservationRequest with ID: [$requestId]")
//            repository.delete(deleteNotification)
//            repository.flush()
//        }
//    }

    fun checkIfHasRatingAllowedNotification(reservationRequest: ReservationRequest): Boolean =
        repository.findAllByTypeAndReservationRequest(NotificationType.RATING_ALLOWED, reservationRequest).isNotEmpty()

}
