package com.project.najdiprevoz.domain

enum class RequestRideStatus(private val status: String) {
    APPROVED("Approved"), DENIED("Denied"), WAITING("Waiting")
}
