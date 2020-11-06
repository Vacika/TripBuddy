package com.project.najdiprevoz.interfaces

interface SmsService {
    fun sendSms(phoneNumber: String, body: String)
}