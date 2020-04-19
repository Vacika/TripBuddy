package com.project.najdiprevoz.web.response

import com.project.najdiprevoz.domain.Rating
import java.util.*

class UserProfileResponse(val id: Long,
                          val firstName: String,
                          val lastName: String,
                          val profilePhoto: ByteArray?,
                          val username: String,
                          val phoneNumber: String?,
                          val gender: String,
                          val birthDate: Date,
                          val ratings: List<Rating>?,
                          val averageRating: Double)