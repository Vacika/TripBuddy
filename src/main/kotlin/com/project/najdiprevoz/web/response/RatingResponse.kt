package com.project.najdiprevoz.web.response

import java.time.ZonedDateTime

class RatingResponse(val id: Long,
                     val fromFullName: String,
                     val fromProfilePic: String?,
                     val fromId: Long,
                     val rating: Int,
                     val note: String?,
                     val tripId: Long,
                     val tripFromLocation: String,
                     val tripToLocation: String,
                     val tripDate: ZonedDateTime)