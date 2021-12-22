package com.project.najdiprevoz.web.response
class PastTripResponse(
        val tripId: Long,
        val driver: UserShortResponse,
        val from: String,
        val to: String,
        val pricePerHead: Int,
        val canSubmitRating: Boolean)