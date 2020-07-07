package com.project.najdiprevoz.api

import com.project.najdiprevoz.domain.User
import com.project.najdiprevoz.services.UserService
import com.project.najdiprevoz.web.request.EditUserProfileRequest
import com.project.najdiprevoz.web.request.create.CreateUserRequest
import com.project.najdiprevoz.web.request.edit.ChangeProfilePhotoRequest
import com.project.najdiprevoz.web.response.UserProfileResponse
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @GetMapping("/{userId}")
    fun findUserById(@PathVariable("userId") userId: Long): User =
            userService.findUserById(userId)

    @GetMapping("/user/{username}")
    fun findUserByUsername(@PathVariable username: String): UserProfileResponse =
            userService.getUserInfo(username)

    @PutMapping("/register")
    fun createUser(@RequestBody request: CreateUserRequest): User =
            userService.createNewUser(request)

    @PutMapping("/edit")
    fun editProfile(@RequestBody req: EditUserProfileRequest, principal: Principal): User =
            userService.editUserProfile(req, principal.name)
}
