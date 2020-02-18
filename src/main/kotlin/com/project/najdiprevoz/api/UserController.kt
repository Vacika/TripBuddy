package com.project.najdiprevoz.api

import com.project.najdiprevoz.services.UserService
import com.project.najdiprevoz.web.request.create.CreateMemberRequest
import com.project.najdiprevoz.web.request.edit.ChangeProfilePhotoRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/members")
class UserController(private val userService: UserService) {

    @GetMapping("/{userId}")
    fun findUserById(@PathVariable("userId") userId: Long) =
            userService.findUserById(userId)

    @GetMapping
    fun findUserByUsername(username: String) =
            userService.findUserByUsername(username)

    @PutMapping
    fun createUser(@RequestBody request: CreateMemberRequest) =
            userService.createNewUser(request)

    @GetMapping("/edit/profile-photo")
    fun editProfilePhoto(request: ChangeProfilePhotoRequest) =
            userService.editProfilePhoto(request)
}