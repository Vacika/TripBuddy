package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.Notification
import com.project.najdiprevoz.enums.NotificationAction
import com.project.najdiprevoz.enums.RequestStatus
import com.project.najdiprevoz.web.response.NotificationResponse
import org.springframework.stereotype.Service

@Service
class NotificationManagementService(private val notificationService: NotificationService,
                                    private val rideRequestService: RideRequestService) {

    fun takeAction(notificationId: Long, action: NotificationAction):List<NotificationResponse> {
        val notification = notificationService.findById(notificationId)
        notificationService.markAsSeen(notificationId)
        notificationService.removeAllActionsForNotification(notificationId)
        when (action) {
            NotificationAction.APPROVE -> rideRequestService.changeStatusByRideRequestId(notification.rideRequest.id, RequestStatus.APPROVED) //driver approves
            NotificationAction.CANCEL -> rideRequestService.changeStatusByRideRequestId(notification.rideRequest.id, RequestStatus.CANCELLED)//this is when the requester decides to cancel their request
            NotificationAction.MARK_AS_SEEN -> notificationService.markAsSeen(notificationId) // just mark seen
            NotificationAction.DENY -> rideRequestService.changeStatusByRideRequestId(notification.rideRequest.id, RequestStatus.DENIED) // driver denies request
        }
        return getUnreadNotifications(notification.to.username)
    }

    fun getMyNotifications(name: String): List<NotificationResponse> {
        return notificationService.getMyNotifications(name).map{mapToNotificationResponse(it)}
    }

    fun getUnreadNotifications(name: String): List<NotificationResponse> {
        return notificationService.getUnreadNotifications(name).map{mapToNotificationResponse(it)}
    }

    private fun mapToNotificationResponse(notification:Notification) = with(notification){
        NotificationResponse(id = id,
                from =from.mapToUserShortResponse(),
                rideRequestId = rideRequest.id,
                type = type,
                actions = actions,
                createdOn = createdOn)
    }
}