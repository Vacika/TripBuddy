package com.project.najdiprevoz.twilio

import com.twilio.http.TwilioRestClient
import com.twilio.rest.api.v2010.account.MessageCreator
import com.twilio.type.PhoneNumber
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class TwilioSmsService(
        @Value("\${twilio.phone-number}") private val twilioPhoneNumber: String,
        @Value("\${twilio.account-sid}") private val twilioAccountSid: String,
        @Value("\${twilio.auth-token}") private val twilioAuthToken: String) {

    fun sendSms(phoneNumber: String, body: String) {
        val client = TwilioRestClient.Builder(twilioAccountSid, twilioAuthToken).build()
        val message = MessageCreator(
                PhoneNumber(phoneNumber),
                PhoneNumber(twilioPhoneNumber),
                body).create(client)
    }
}