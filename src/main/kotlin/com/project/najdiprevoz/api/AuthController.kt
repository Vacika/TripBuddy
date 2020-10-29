package com.project.najdiprevoz.api

import com.project.najdiprevoz.security.UserDetailsServiceImpl
import com.project.najdiprevoz.web.request.LoginUserRequest
import com.project.najdiprevoz.web.response.JwtResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import security.JwtTokenUtil
import java.util.*


@RestController
@RequestMapping("/api/authenticate")
class AuthController(private val userDetailsServiceImpl: UserDetailsServiceImpl,
                     private val authenticationManager: AuthenticationManager) {

    val jwtTokenUtil = JwtTokenUtil()

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
            throw Exception("USER_DISABLED", e)
        } catch (e: BadCredentialsException) {
            throw Exception("INVALID_CREDENTIALS", e)
        }
    }
}