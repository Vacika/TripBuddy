package com.project.najdiprevoz.domain

import com.project.najdiprevoz.enums.Language
import java.time.ZonedDateTime
import javax.persistence.*

@Entity
@Table(name="sms_trip_notifications")
class SmsTripNotification(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0L,

        @Column(name = "created_on")
        val createdOn: ZonedDateTime,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "from_location", referencedColumnName = "id")
        val fromLocation: City,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "to_location", referencedColumnName = "id")
        val destination: City,

        @Column(name = "valid_until")
        val validUntil: ZonedDateTime,

        @Column(name = "phone_number")
        val phoneNumber: String,

        @Enumerated(EnumType.STRING)
        @Column(name = "lang")
        val language: Language
) {
}
