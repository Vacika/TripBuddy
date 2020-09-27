package com.project.najdiprevoz.api
import com.project.najdiprevoz.domain.User
import com.project.najdiprevoz.exceptions.TokenNotValidOrExpiredException
import com.project.najdiprevoz.repositories.PasswordResetTokenRepository
import com.project.najdiprevoz.services.UserService
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam


@Controller
@RequestMapping("/api/reset-password")
class PasswordResetController(private val userService: UserService,
                              private val tokenRepository: PasswordResetTokenRepository) {

    @GetMapping
    fun isTokenValid(@RequestParam(required = false) token: String): Boolean {
        val resetToken = tokenRepository.findByToken(token)
        if (resetToken != null && !resetToken.isExpired()) {
            return true
        }
        else {
            throw TokenNotValidOrExpiredException("Token was either invalid or expired. Token: $token")
        }
    }

    @PostMapping
    @Transactional
    fun handlePasswordReset(tokenSent: String, newPassword: String) {
        val token = tokenRepository.findByToken(tokenSent)
        val user: User = token!!.user
        userService.updatePassword(newPassword, user.id)
        tokenRepository.delete(token)
    }
}
