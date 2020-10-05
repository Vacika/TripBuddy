package com.project.najdiprevoz.api

import com.project.najdiprevoz.domain.User
import com.project.najdiprevoz.services.UserService
import com.project.najdiprevoz.web.request.EditUserProfileRequest
import com.project.najdiprevoz.web.request.create.CreateUserRequest
import com.project.najdiprevoz.web.response.UserProfileResponse
import com.project.najdiprevoz.web.response.UserResponse
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @GetMapping("/details/{userId}")
    fun getUserInfo(@PathVariable("userId") userId: Long): UserProfileResponse =
            userService.getUserInfo(userId)

    @PutMapping("/register")
    fun createUser(@RequestBody request: CreateUserRequest): User =
            userService.createNewUser(request)

    @GetMapping("/activate")
    fun activateUser(@RequestParam(required = false) activationToken: String): Boolean =
            userService.activateUser(activationToken)

    @PutMapping("/edit")
    fun editProfile(@RequestBody req: EditUserProfileRequest, principal: Principal): User =
            userService.editUserProfile(req, principal.name)
}
