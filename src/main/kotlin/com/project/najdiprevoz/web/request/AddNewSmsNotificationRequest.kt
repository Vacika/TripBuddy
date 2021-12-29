package com.project.najdiprevoz.web.request

import java.time.ZonedDateTime
//from = fromDestination, to = toDestination
class AddNewSmsNotificationRequest(val from: Long, val to: Long, val validFor: ZonedDateTime, val phone: String, val language: String)
