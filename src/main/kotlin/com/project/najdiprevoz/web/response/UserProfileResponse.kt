package com.project.najdiprevoz.web.response

import com.project.najdiprevoz.domain.Rating
import java.time.ZonedDateTime
import java.util.*

class UserProfileResponse (
        id: Long,
        firstName: String,
        lastName: String,
        profilePhoto: String?,
        username: String,
        phoneNumber: String?,
        gender: String,
        birthDate: Date,
        ratings: List<Rating>?,
        averageRating: Double,
        defaultLanguage: String,
        val publishedTrips: Int,
        val memberSince: ZonedDateTime
) : UserResponse(id, firstName, lastName, profilePhoto, username, phoneNumber, gender, birthDate, ratings, averageRating, defaultLanguage)