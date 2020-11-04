package com.project.najdiprevoz.repositories

import com.project.najdiprevoz.domain.City
import com.project.najdiprevoz.domain.SmsTripNotification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.ZonedDateTime

@Repository
interface SmsTripNotificationRepository : JpaRepository<SmsTripNotification, Long> {

    fun findAllByFromLocationAndDestinationAndValidUntilIsAfter(fromLocation: City, toLocation: City, timeNow: ZonedDateTime) : List<SmsTripNotification>
}