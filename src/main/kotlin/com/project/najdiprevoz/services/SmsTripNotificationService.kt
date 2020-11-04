package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.City
import com.project.najdiprevoz.domain.SmsTripNotification
import com.project.najdiprevoz.repositories.SmsTripNotificationRepository
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class SmsTripNotificationService(
        private val cityService: CityService,
        private val repository: SmsTripNotificationRepository) {

    fun addNewNotification(fromLocation: String, toLocation: String, validUntil: ZonedDateTime): SmsTripNotification {
        val startingPoint = cityService.findByName(fromLocation)
        val endingPoint = cityService.findByName(toLocation)
        val result = createSmsTripObject(startingPoint, endingPoint, validUntil)
        return repository.save(result)
    }

    private fun createSmsTripObject(startingPoint: City, endingPoint: City, validUntil: ZonedDateTime): SmsTripNotification {
        return SmsTripNotification(0L, ZonedDateTime.now(), startingPoint, endingPoint, validUntil);
    }
}