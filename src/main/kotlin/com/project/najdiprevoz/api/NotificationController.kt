package com.project.najdiprevoz.api

import com.project.najdiprevoz.services.NotificationService
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/notifications")
class NotificationController (private val service: NotificationService){

    @GetMapping
    fun getMyNotifications(principal: Principal) = service.getMyNotifications(principal.name)

    @GetMapping("/unread")
    fun getUnreadNotifications(principal: Principal) = service.getUnreadNotifications(principal.name)

    @PutMapping("/{notificationId}/seen")
    fun markAsSeen(@PathVariable("notificationId") notificationId: Long) = service.markAsSeen(notificationId)
}