package com.project.najdiprevoz.web.response

import com.project.najdiprevoz.enums.NotificationAction
import com.project.najdiprevoz.enums.NotificationType
import java.time.ZonedDateTime

class NotificationResponse(val id: Long,
                           val fromId: Long,
                           val fromName: String,
                           val actions: List<NotificationAction>,
                           val createdOn: ZonedDateTime,
                           val rideRequestId: Long,
                           val type: NotificationType,
                           val seen: Boolean)