package com.project.najdiprevoz.web.response

import java.time.ZonedDateTime

class RatingResponse(val id: Long,
                     val fromFullName: String,
                     val fromProfilePic: String?,
                     val fromId: Long,
                     val rating: Int,
                     val note: String?,
                     val rideId: Long,
                     val rideFrom: String,
                     val rideTo: String,
                     val rideDate: ZonedDateTime)