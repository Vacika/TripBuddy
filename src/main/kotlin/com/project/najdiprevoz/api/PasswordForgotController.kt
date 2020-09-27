package com.project.najdiprevoz.api

import com.project.najdiprevoz.domain.Mail
import com.project.najdiprevoz.domain.PasswordResetToken
import com.project.najdiprevoz.domain.User
import com.project.najdiprevoz.repositories.PasswordResetTokenRepository
import com.project.najdiprevoz.services.EmailService
import com.project.najdiprevoz.services.UserService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.util.*


@Controller
@RequestMapping("/api/forgot-password")
class PasswordForgotController(private val userService: UserService,
                               private val tokenRepository: PasswordResetTokenRepository,
                               private val emailService: EmailService) {


    @PostMapping
    fun processForgotPasswordForm(username: String) {
        val user: User? = userService.findUserByUsername(username)
        val token = PasswordResetToken(
                token = UUID.randomUUID().toString(),
                user = user!!)
        token.setExpiryDate(30)
        tokenRepository.save(token)
        val mail = Mail()
        mail.from = "no-reply@memorynotfound.com"
        mail.to = username
        mail.subject = "Password reset request"
        val model: MutableMap<String, Any> = HashMap()
        model["token"] = token
        model["user"] = user
        //TODO: Move this to application.yml
        model["signature"] = "https://najdiprevoz.com.mk"
        val url = String.format("https://najdiprevoz.com.mk")
        model["resetUrl"] = url + "/reset-password?token=" + token.token
        mail.model = model
        emailService.sendEmail(mail)
    }
}
