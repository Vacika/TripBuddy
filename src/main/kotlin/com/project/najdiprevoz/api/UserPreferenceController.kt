package com.project.najdiprevoz.api

import com.project.najdiprevoz.services.UserPreferenceService
import com.project.najdiprevoz.web.request.edit.EditUserPreferenceRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/preferences")
class UserPreferenceController(private val service: UserPreferenceService) {

    @GetMapping("/{userId}")
    fun getPreferenceByUser(@PathVariable("userId") userId: Long) =
            service.getUserPreference(userId)

    @GetMapping("/edit/{userId}")
    fun editPreferenceByUser(@PathVariable("userId") userId: Long, request: EditUserPreferenceRequest) =
            service.editUserPreferenceRequest(userId, request)
}