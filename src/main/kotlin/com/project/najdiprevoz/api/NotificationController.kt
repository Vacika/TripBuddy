package com.project.najdiprevoz.api

import com.project.najdiprevoz.enums.NotificationAction
import com.project.najdiprevoz.services.management.NotificationManagementService
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/notifications")
class NotificationController(private val service: NotificationManagementService) {

    @GetMapping
    fun getMyNotifications(principal: Principal) =
            service.getMyNotifications(principal.name)

    @PutMapping("/{notificationId}/action")
    fun takeAction(@PathVariable("notificationId") notificationId: Long, @RequestBody action: String) =
            service.executeNotificationAction(notificationId, NotificationAction.valueOf(action))
}