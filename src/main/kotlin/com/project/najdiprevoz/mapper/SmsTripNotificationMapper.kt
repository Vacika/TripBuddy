package com.project.najdiprevoz.mapper

import com.project.najdiprevoz.services.sms.SmsTripNotificationService
import com.project.najdiprevoz.web.request.AddNewSmsNotificationRequest
import org.springframework.stereotype.Service

@Service
class SmsTripNotificationMapper(private val service: SmsTripNotificationService) {

    fun addNewNotification(req: AddNewSmsNotificationRequest) = with(req) {
        service.addNewNotification(fromLocation = from, toLocation = to, validUntil = validFor, phone = phone, language = language)
    }
}
