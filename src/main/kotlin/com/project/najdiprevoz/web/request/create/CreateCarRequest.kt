package com.project.najdiprevoz.web.request.create

class CreateCarRequest(val brand: String,
                       val model: String,
                       val totalSeats: Int,
                       val yearManufacture: Int,
                       val ownerId: Long)