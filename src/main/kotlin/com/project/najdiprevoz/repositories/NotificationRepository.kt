package com.project.najdiprevoz.repositories

import com.project.najdiprevoz.domain.Notification
import com.project.najdiprevoz.domain.ReservationRequest
import com.project.najdiprevoz.enums.NotificationType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface NotificationRepository : JpaRepository<Notification, Long> {
    fun findAllByToUsernameOrderByCreatedOnDesc(username: String): List<Notification>
    fun findAllByToUsernameAndSeenIsFalseOrderByCreatedOnDesc(username: String): List<Notification>
    fun findAllByReservationRequest_Id(ReservationRequestId: Long): List<Notification>
    @Modifying
    @Query("UPDATE Notification SET seen = true where id = :notificationId")
    fun markAsSeen(@Param("notificationId") notificationId: Long)

    fun findAllByTypeAndReservationRequest(type: NotificationType, reservationRequest: ReservationRequest) : List<Notification>
}