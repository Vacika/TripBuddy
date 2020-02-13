package com.project.najdiprevoz.web.request.create

class CreateRatingRequest(val rating: Int,
                          val authorId: Long,
                          val rideId: Long,
                          val note: String?)