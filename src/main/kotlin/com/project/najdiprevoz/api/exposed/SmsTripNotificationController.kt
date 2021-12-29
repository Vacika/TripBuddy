package com.project.najdiprevoz.api.exposed

import com.project.najdiprevoz.mapper.SmsTripNotificationMapper
import com.project.najdiprevoz.web.request.AddNewSmsNotificationRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/public/sms-notifications")
class SmsTripNotificationController(private val mapper: SmsTripNotificationMapper) {

    @PostMapping("/new")
    fun addNewSmsNotification(@RequestBody req: AddNewSmsNotificationRequest) =
            mapper.addNewNotification(req)
}