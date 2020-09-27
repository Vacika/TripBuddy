package com.project.najdiprevoz.api

import com.project.najdiprevoz.services.PasswordForgotService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping


@Controller
@RequestMapping("/api/forgot-password")
class PasswordForgotController(private val passwordForgotService: PasswordForgotService) {


    @PostMapping
    fun processForgotPasswordForm(username: String) =
            passwordForgotService.createResetTokenForUser(username)
}
