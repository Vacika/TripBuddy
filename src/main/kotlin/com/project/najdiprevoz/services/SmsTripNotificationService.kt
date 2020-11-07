package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.City
import com.project.najdiprevoz.domain.SmsTripNotification
import com.project.najdiprevoz.domain.Trip
import com.project.najdiprevoz.enums.Language
import com.project.najdiprevoz.interfaces.SmsService
import com.project.najdiprevoz.repositories.SmsTripNotificationRepository
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


    fun addNewNotification(fromLocation: Long, toLocation: Long, validUntil: ZonedDateTime, phone: String, language: String): SmsTripNotification {
        val startingPoint = cityService.findById(fromLocation)
        val endingPoint = cityService.findById(toLocation)
        val result = createSmsTripObject(startingPoint, endingPoint, validUntil, phone, language)
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

        smsList.forEach { smsService.sendSms(it.phoneNumber, createSmsBody(trip, it.language)) }
    }

    private fun createSmsTripObject(startingPoint: City, endingPoint: City, validUntil: ZonedDateTime, phone: String, language: String): SmsTripNotification {
        return SmsTripNotification(0L, ZonedDateTime.now(), startingPoint, endingPoint, validUntil, phone, Language.valueOf(language))
    }

    //TODO: Make SMS body fetch from translations
    private fun createSmsBody(trip: Trip, language: Language): String {
        val formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm")
        val dateFormatted = trip.departureTime.format(formatter)
        logger.debug("Creating SMS Body in ${language.name} language!")
        return when (language) {
            Language.MK -> String.format("[%s] Објавен е нов оглас од %s до %s на %s.  %s", applicationName, trip.fromLocation.name,
                    trip.destination.name, dateFormatted, appLink)
            Language.EN -> String.format("[%s] A new trip was published from %s to %s by %s on %s. Please check it on %s", applicationName, trip.fromLocation.name,
                    trip.destination.name, trip.driver.getFullName(), dateFormatted, appLink)
            Language.AL -> String.format("[%s] A new trip was published from %s to %s by %s on %s. Please check it on %s", applicationName, trip.fromLocation.name,
                    trip.destination.name, trip.driver.getFullName(), dateFormatted, appLink)
        }
    }
}
