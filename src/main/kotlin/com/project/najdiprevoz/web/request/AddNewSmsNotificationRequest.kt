package com.project.najdiprevoz.web.request

import java.time.ZonedDateTime

class AddNewSmsNotificationRequest(val from: Long, val to: Long, val validFor: ZonedDateTime, val phone: String, val language: String)
