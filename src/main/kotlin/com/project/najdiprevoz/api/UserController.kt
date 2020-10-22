package com.project.najdiprevoz.api

import com.project.najdiprevoz.domain.User
import com.project.najdiprevoz.mapper.UserMapper
import com.project.najdiprevoz.web.request.EditUserProfileRequest
import com.project.najdiprevoz.web.request.create.CreateUserRequest
import com.project.najdiprevoz.web.response.UserProfileResponse
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/users")
class UserController(private val mapper: UserMapper) {

    @GetMapping("/details/{userId}")
    fun getUserInfo(@PathVariable("userId") userId: Long): UserProfileResponse =
            mapper.getUserInfo(userId)

    @PutMapping("/register")
    fun createUser(@RequestBody request: CreateUserRequest): User =
            mapper.createNewUser(request)

    @GetMapping("/activate")
    fun activateUser(@RequestParam(required = false) activationToken: String): Boolean =
            mapper.activateUser(activationToken)

    @PutMapping("/edit")
    fun editProfile(@RequestBody req: EditUserProfileRequest, principal: Principal): User =
            mapper.editUserProfile(req, principal.name)
}
