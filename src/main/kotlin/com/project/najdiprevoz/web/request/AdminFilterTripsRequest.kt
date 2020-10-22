package com.project.najdiprevoz.web.request

import java.time.ZonedDateTime

class AdminFilterTripsRequest(val from: String?,
                              val to: String?,
                              val requestedSeats: Int?,
                              val date: ZonedDateTime?,
                              val page: Int = 1,
                              val pageSize: Int = 10)
