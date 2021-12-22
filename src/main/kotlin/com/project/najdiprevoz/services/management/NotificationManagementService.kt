package com.project.najdiprevoz.services.management

import com.project.najdiprevoz.domain.Notification
import com.project.najdiprevoz.enums.NotificationAction
import com.project.najdiprevoz.enums.ReservationStatus
import com.project.najdiprevoz.services.NotificationService
import com.project.najdiprevoz.services.ReservationRequestService
import com.project.najdiprevoz.web.response.NotificationResponse
import org.springframework.stereotype.Service

@Service
class NotificationManagementService(private val notificationService: NotificationService,
                                    private val reservationRequestService: ReservationRequestService
) {

    fun takeAction(notificationId: Long, action: NotificationAction): List<NotificationResponse> {
        val notification = notificationService.findById(notificationId)
        notificationService.markAsSeen(notificationId)
        when (action) {
            NotificationAction.APPROVE -> reservationRequestService.changeStatus(notification.reservationRequest.id, ReservationStatus.APPROVED) //driver approves
            NotificationAction.CANCEL_RESERVATION -> reservationRequestService.changeStatus(notification.reservationRequest.id, ReservationStatus.CANCELLED)//this is when the requester decides to cancel their request
            NotificationAction.MARK_AS_SEEN -> notificationService.markAsSeen(notificationId) // just mark seen
            NotificationAction.DENY -> reservationRequestService.changeStatus(notification.reservationRequest.id, ReservationStatus.DENIED) // driver denies request
            NotificationAction.SUBMIT_RATING -> TODO()
        }
        return getMyNotifications(notification.to.username)
    }

    fun getMyNotifications(name: String): List<NotificationResponse> {
        return notificationService.getMyNotifications(name).map { mapToNotificationResponse(it) }
    }

    private fun mapToNotificationResponse(notification: Notification) = with(notification) {
        NotificationResponse(id = id,
                fromId = from.id,
                fromName = from.getFullName(),
                fromProfilePic = from.profilePhoto,
                reservationRequestId = reservationRequest.id,
                type = type,
                actions = actions,
                createdOn = createdOn,
                seen = seen)
    }
}
