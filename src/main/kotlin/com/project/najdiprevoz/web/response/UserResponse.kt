package com.project.najdiprevoz.web.response

import com.project.najdiprevoz.domain.Rating
import java.time.ZonedDateTime
import java.util.*

open class UserResponse(val id: Long,
                        val firstName: String,
                        val lastName: String,
                        val profilePhoto: String?,
                        val username: String,
                        val phoneNumber: String?,
                        val gender: String,
                        val birthDate: Date,
                        val ratings: List<Rating>?,
                        val averageRating: Double,
                        val defaultLanguage: String)

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
        val publishedRides: Int,
        val memberSince: ZonedDateTime
) : UserResponse(id, firstName, lastName, profilePhoto, username, phoneNumber, gender, birthDate, ratings, averageRating, defaultLanguage)