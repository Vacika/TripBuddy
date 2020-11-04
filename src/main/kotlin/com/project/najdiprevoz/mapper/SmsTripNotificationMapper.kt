package com.project.najdiprevoz.mapper

import com.project.najdiprevoz.services.SmsTripNotificationService
import com.project.najdiprevoz.web.request.AddNewSmsNotificationRequest
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class SmsTripNotificationMapper(private val service: SmsTripNotificationService) {

    fun addNewNotification(req: AddNewSmsNotificationRequest) = with(req) {
        val validUntil = ZonedDateTime.now().plusDays(validFor.toLong())
        service.addNewNotification(fromLocation = from, toLocation = to, validUntil = validUntil, phone = phone)
    }
}