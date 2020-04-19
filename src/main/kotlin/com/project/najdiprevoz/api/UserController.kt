package com.project.najdiprevoz.api

import com.project.najdiprevoz.services.UserService
import com.project.najdiprevoz.web.request.EditUserProfileRequest
import com.project.najdiprevoz.web.request.create.CreateUserRequest
import com.project.najdiprevoz.web.request.edit.ChangeProfilePhotoRequest
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @GetMapping("/{userId}")
    fun findUserById(@PathVariable("userId") userId: Long) =
            userService.findUserById(userId)

    @GetMapping
    fun findUserByUsername(username: String) =
            userService.findUserByUsername(username)

    @PutMapping("/register")
    fun createUser(@RequestBody request: CreateUserRequest) =
            userService.createNewUser(request)

    @PostMapping("/edit/profile-photo")
    fun editProfilePhoto(@RequestBody request: ChangeProfilePhotoRequest) =
            userService.editProfilePhoto(request)

    @PutMapping("/edit/password")
    fun changePassword(password: String, principal: Principal) =
            userService.changePassword(password, principal.name)

    @PutMapping("/edit")
    fun editProfile(@RequestBody req: EditUserProfileRequest, principal: Principal) =
            userService.editUserProfile(req, principal.name)
}