package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.City
import com.project.najdiprevoz.domain.SmsTripNotification
import com.project.najdiprevoz.domain.Trip
import com.project.najdiprevoz.repositories.SmsTripNotificationRepository
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class SmsTripNotificationService(
        private val cityService: CityService,
        private val repository: SmsTripNotificationRepository) {

    fun addNewNotification(fromLocation: String, toLocation: String, validUntil: ZonedDateTime, phone: String): SmsTripNotification {
        val startingPoint = cityService.findByName(fromLocation)
        val endingPoint = cityService.findByName(toLocation)
        val result = createSmsTripObject(startingPoint, endingPoint, validUntil, phone)
        return repository.save(result)
    }

    /***
    This function gets called only when publishing a new Trip, and will check if there are any SMS's to be sent.
     */
    fun checkForSmsNotifications(trip: Trip) = with(trip) {
        val smsList = repository.findAllByFromLocationAndDestinationAndValidUntilIsAfter(fromLocation, destination, ZonedDateTime.now())
        val body = createSmsBody(trip)

    }

    private fun createSmsTripObject(startingPoint: City, endingPoint: City, validUntil: ZonedDateTime, phone: String): SmsTripNotification {
        return SmsTripNotification(0L, ZonedDateTime.now(), startingPoint, endingPoint, validUntil, phone);
    }

    //TODO:
    private fun createSmsBody(trip: Trip): String{
        return String.format("")
    }
}