package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.City
import com.project.najdiprevoz.domain.SmsTripNotification
import com.project.najdiprevoz.domain.Trip
import com.project.najdiprevoz.interfaces.SmsService
import com.project.najdiprevoz.repositories.SmsTripNotificationRepository
import com.project.najdiprevoz.twilio.TwilioSmsService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Service
class SmsTripNotificationService(
        private val cityService: CityService,
        private val smsService: SmsService,
        private val repository: SmsTripNotificationRepository,
        @Value("\${najdiprevoz.signature}") private val applicationName: String,
        @Value("\${najdiprevoz.official-app-link}") private val appLink: String) {

    val logger: Logger = LoggerFactory.getLogger(SmsTripNotificationService::class.java)
    fun addNewNotification(fromLocation: Long, toLocation: Long, validUntil: ZonedDateTime, phone: String): SmsTripNotification {
        val startingPoint = cityService.findById(fromLocation)
        val endingPoint = cityService.findById(toLocation)
        val result = createSmsTripObject(startingPoint, endingPoint, validUntil, phone)
        logger.info("Creating new SMS notification, from ${startingPoint.name} to ${endingPoint.name}, validUntil: ${validUntil}")
        return repository.save(result)
    }

    /***
    This function gets called only when publishing a new Trip, and will check if there are any SMS's to be sent.
     */
    fun checkForSmsNotifications(trip: Trip) = with(trip) {
        logger.info("Checking for SMS notifications for a trip from ${trip.fromLocation.name} to ${trip.destination.name}")
        val smsList = repository.findAllByFromLocationAndDestinationAndValidUntilIsAfter(fromLocation, destination, ZonedDateTime.now())
        logger.info("Found ${smsList.size} sms notifications adequate for this trip.")
        val body = createSmsBody(trip)
        smsList.forEach { smsService.sendSms(it.phoneNumber, body) }
    }

    private fun createSmsTripObject(startingPoint: City, endingPoint: City, validUntil: ZonedDateTime, phone: String): SmsTripNotification {
        return SmsTripNotification(0L, ZonedDateTime.now(), startingPoint, endingPoint, validUntil, phone)
    }

    //TODO: Make SMS body into different languages (add new column for language into table)
    private fun createSmsBody(trip: Trip): String {
        val formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm")
        val dateFormatted = trip.departureTime.format(formatter)
        return String.format("[%s] A new trip was published from %s to %s by %s on %s. Please check it on %s",applicationName, trip.fromLocation.name,
                trip.destination.name, trip.driver.getFullName(), dateFormatted, appLink)
    }
}