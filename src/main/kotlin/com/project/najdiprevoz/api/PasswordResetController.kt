package com.project.najdiprevoz.api
import com.project.najdiprevoz.services.PasswordForgotService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam


@Controller
@RequestMapping("/api/reset-password")
class PasswordResetController(private val passwordForgotService: PasswordForgotService) {

    @GetMapping
    fun isTokenValid(@RequestParam(required = false) token: String): Boolean  =
            passwordForgotService.isTokenValid(token)

    @PostMapping
    fun handlePasswordReset(tokenSent: String, newPassword: String) =
            passwordForgotService.handlePasswordReset(tokenSent, newPassword)
}
