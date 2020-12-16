package com.project.najdiprevoz.api

import com.project.najdiprevoz.security.JwtTokenUtil
import com.project.najdiprevoz.security.UserDetailsServiceImpl
import com.project.najdiprevoz.web.request.LoginUserRequest
import com.project.najdiprevoz.web.response.JwtResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*


@RestController
@RequestMapping("/api/authenticate")
class AuthController(private val userDetailsServiceImpl: UserDetailsServiceImpl,
                     private val authenticationManager: AuthenticationManager,
                     private val jwtTokenUtil: JwtTokenUtil) {

    val logger: Logger = LoggerFactory.getLogger(AuthController::class.java)

    @RequestMapping
    fun login(@RequestBody req: LoginUserRequest): JwtResponse = with(req) {
        authenticate(username, password);

        val userDetails = userDetailsServiceImpl
                .loadUserByUsername(username);

        val token = jwtTokenUtil.generateToken(userDetails)

        return JwtResponse(token, userDetails)
    }

    fun authenticate(username: String, password: String) {
        Objects.requireNonNull(username)
        Objects.requireNonNull(password)
        try {
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password))
        } catch (e: DisabledException) {
            logger.debug("User $username is disabled!")
            throw DisabledException("USER_DISABLED", e)
        } catch (e: BadCredentialsException) {
            logger.debug("User $username entered wrong credentials!")
            throw BadCredentialsException("INVALID_CREDENTIALS")
        }
    }
}
