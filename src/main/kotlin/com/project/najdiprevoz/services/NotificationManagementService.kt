package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.Notification
import com.project.najdiprevoz.enums.NotificationAction
import com.project.najdiprevoz.enums.RideRequestStatus
import com.project.najdiprevoz.web.response.NotificationResponse
import org.springframework.stereotype.Service

@Service
class NotificationManagementService(private val notificationService: NotificationService,
                                    private val rideRequestService: RideRequestService) {

    fun takeAction(notificationId: Long, action: NotificationAction): List<NotificationResponse> {
        val notification = notificationService.findById(notificationId)
        notificationService.markAsSeen(notificationId)
        when (action) {
            NotificationAction.APPROVE -> rideRequestService.changeStatus(notification.rideRequest.id, RideRequestStatus.APPROVED) //driver approves
            NotificationAction.CANCEL -> rideRequestService.changeStatus(notification.rideRequest.id, RideRequestStatus.CANCELLED)//this is when the requester decides to cancel their request
            NotificationAction.MARK_AS_SEEN -> notificationService.markAsSeen(notificationId) // just mark seen
            NotificationAction.DENY -> rideRequestService.changeStatus(notification.rideRequest.id, RideRequestStatus.DENIED) // driver denies request
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
                rideRequestId = rideRequest.id,
                type = type,
                actions = actions,
                createdOn = createdOn,
                seen = seen)
    }
}
