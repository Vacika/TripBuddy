package com.project.najdiprevoz.services.sms

import com.project.najdiprevoz.interfaces.SmsService
import com.twilio.http.TwilioRestClient
import com.twilio.rest.api.v2010.account.MessageCreator
import com.twilio.type.PhoneNumber
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class TwilioSmsService(
    @Value("\${twilio.phone-number}") private val twilioPhoneNumber: String,
    @Value("\${twilio.account-sid}") private val twilioAccountSid: String,
    @Value("\${twilio.auth-token}") private val twilioAuthToken: String
) : SmsService {

    val logger: Logger = LoggerFactory.getLogger(TwilioSmsService::class.java)
    override fun sendSms(phoneNumber: String, body: String) {
        val client = TwilioRestClient.Builder(twilioAccountSid, twilioAuthToken).build()
        logger.debug("Sending sms message to $phoneNumber. Body: [$body]")
        val message = MessageCreator(
            PhoneNumber(phoneNumber),
            PhoneNumber(twilioPhoneNumber),
            body
        ).create(client).body
    }
}