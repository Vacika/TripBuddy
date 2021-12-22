package com.project.najdiprevoz.api.exposed
import com.project.najdiprevoz.services.PasswordForgotService
import com.project.najdiprevoz.web.request.HandlePasswordResetRequest
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/public/reset-password")
class PasswordResetController(private val passwordForgotService: PasswordForgotService) {

    @GetMapping
    fun validateToken(@RequestParam(required = false) token: String): Boolean  =
            passwordForgotService.isTokenValid(token)

    @PostMapping
    fun handlePasswordReset(@RequestBody req: HandlePasswordResetRequest) =
            passwordForgotService.handlePasswordReset(req.token, req.password)
}
