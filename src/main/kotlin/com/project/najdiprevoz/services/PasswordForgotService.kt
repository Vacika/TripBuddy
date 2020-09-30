package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.Mail
import com.project.najdiprevoz.domain.PasswordResetToken
import com.project.najdiprevoz.domain.User
import com.project.najdiprevoz.exceptions.TokenNotValidOrExpiredException
import com.project.najdiprevoz.repositories.PasswordResetTokenRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class PasswordForgotService(private val userService: UserService,
                            private val tokenRepository: PasswordResetTokenRepository,
                            private val emailService: EmailService,
                            @Value("\${najdiprevoz.official-app-link}") private val officialAppUrl: String) {

    val logger: Logger = LoggerFactory.getLogger(PasswordForgotService::class.java)
    @Transactional
    fun createResetTokenForUser(username: String) {
        logger.debug("Creating reset token for user: $username")
        val user: User? = userService.findUserByUsername(username)
        val token = PasswordResetToken(
                token = UUID.randomUUID().toString(),
                user = user!!)
        token.setExpiryDate(30)
        logger.debug("Set token expire 30 mins from now.")
        tokenRepository.save(token)
        logger.debug("Saving token.. Token: ${token.token}")
        val mail = Mail()
        mail.lang = user.defaultLanguage.name
        mail.from = "no-reply@najdiprevoz.com.mk"
        mail.to = username
        mail.subject = "Password reset request"
        val model: MutableMap<String, Any> = HashMap()
        model["token"] = token
        model["user"] = user
        //TODO: Move this to application.yml
        model["signature"] = officialAppUrl
        model["resetUrl"] = officialAppUrl + "/reset-password?token=" + token.token
        logger.debug("Reset url for $username is: ${officialAppUrl + "/reset-password?token=" + token.token}")
        mail.model = model
        emailService.sendForgetPasswordMail(mail)
    }

    fun isTokenValid(token: String): Boolean {
        val resetToken = tokenRepository.findByToken(token)
        if (resetToken != null && !resetToken.isExpired()) {
            return true
        } else {
            throw TokenNotValidOrExpiredException("Token was either invalid or expired. Token: $token")
        }
    }

    @Transactional
    fun handlePasswordReset(tokenSent: String, newPassword: String) {
        val token = tokenRepository.findByToken(tokenSent)
        val user: User = token!!.user
        userService.updatePassword(newPassword, user.id)
        tokenRepository.delete(token)
    }
}
