package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.Notification
import com.project.najdiprevoz.enums.NotificationAction
import com.project.najdiprevoz.enums.RideRequestStatus
import com.project.najdiprevoz.web.request.create.CreateRatingRequest
import com.project.najdiprevoz.web.response.NotificationResponse
import org.springframework.stereotype.Service

@Service
class NotificationManagementService(private val notificationService: NotificationService,
                                    private val rideRequestService: RideRequestService,
                                    private val ratingService: RatingService) {

    fun takeAction(notificationId: Long, action: NotificationAction): List<NotificationResponse> {
        val notification = notificationService.findById(notificationId)
        notificationService.markAsSeen(notificationId)
        when (action) {
            NotificationAction.APPROVE -> rideRequestService.changeStatus(notification.rideRequest.id, RideRequestStatus.APPROVED) //driver approves
            NotificationAction.CANCEL -> rideRequestService.changeStatus(notification.rideRequest.id, RideRequestStatus.CANCELLED)//this is when the requester decides to cancel their request
            NotificationAction.MARK_AS_SEEN -> notificationService.markAsSeen(notificationId) // just mark seen
            NotificationAction.DENY -> rideRequestService.changeStatus(notification.rideRequest.id, RideRequestStatus.DENIED) // driver denies request
        }
        if (action != NotificationAction.MARK_AS_SEEN) {
            notificationService.removeAllActionsForNotification(notificationId)
        }
        return getUnreadNotifications(notification.to.username)
    }

    fun getMyNotifications(name: String): List<NotificationResponse> {
        return notificationService.getMyNotifications(name).map { mapToNotificationResponse(it) }
    }

    fun getUnreadNotifications(name: String): List<NotificationResponse> {
        return notificationService.getUnreadNotifications(name).map { mapToNotificationResponse(it) }
    }

    fun removeLastNotificationForRideRequest(requestId: Long): Any = removeLastNotificationForRideRequest(requestId)

    private fun mapToNotificationResponse(notification: Notification) = with(notification) {
        NotificationResponse(id = id,
                fromId = from.id,
                fromName = from.getFullName(),
                rideRequestId = rideRequest.id,
                type = type,
                actions = actions,
                createdOn = createdOn,
                seen = seen)
    }
}