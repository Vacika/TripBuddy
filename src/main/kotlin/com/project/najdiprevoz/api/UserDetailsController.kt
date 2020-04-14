package com.project.najdiprevoz.api

import com.project.najdiprevoz.security.UserDetailsServiceImpl
import com.project.najdiprevoz.web.request.LoginUserRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/login")
class UserDetailsController(private val userDetailsServiceImpl: UserDetailsServiceImpl) {

    @PostMapping
    fun login(req: LoginUserRequest) = userDetailsServiceImpl.loadUserByUsername(req.username)
}