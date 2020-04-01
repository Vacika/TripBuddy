package com.project.najdiprevoz.repositories

import com.project.najdiprevoz.domain.Notification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NotificationRepository : JpaRepository<Notification, Long> {
    fun findAllByToUsername(username: String): List<Notification>
    fun findByRideRequestId(rideRequestId: Long): Notification
}