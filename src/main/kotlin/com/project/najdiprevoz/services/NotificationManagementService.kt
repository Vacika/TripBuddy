package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.Notification
import com.project.najdiprevoz.enums.NotificationAction
import com.project.najdiprevoz.enums.RequestStatus
import org.springframework.stereotype.Service

@Service
class NotificationManagementService(private val notificationService: NotificationService,
                                    private val rideRequestService: RideRequestService) {

    fun takeAction(notificationId: Long, action: NotificationAction) {
        val notification = notificationService.findById(notificationId)
        notificationService.markAsSeen(notificationId)
        when (action) {
            NotificationAction.APPROVE -> rideRequestService.changeStatusByRideRequestId(notification.rideRequest.id, RequestStatus.APPROVED) //driver approves
            NotificationAction.CANCEL -> rideRequestService.changeStatusByRideRequestId(notification.rideRequest.id, RequestStatus.CANCELLED)//this is when the requester decides to cancel their request
            NotificationAction.MARK_AS_SEEN -> notificationService.markAsSeen(notificationId) // just mark seen
            NotificationAction.DENY -> rideRequestService.changeStatusByRideRequestId(notification.rideRequest.id, RequestStatus.DENIED) // driver denies request
        }
    }

    fun getMyNotifications(name: String): List<Notification> {
        return notificationService.getMyNotifications(name)
    }

    fun getUnreadNotifications(name: String): List<Notification> {
        return notificationService.getUnreadNotifications(name)
    }
}