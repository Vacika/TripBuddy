package com.project.najdiprevoz.api

import com.project.najdiprevoz.services.UserService
import com.project.najdiprevoz.web.request.create.CreateMemberRequest
import com.project.najdiprevoz.web.request.edit.ChangeProfilePhotoRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/members")
class UserController(private val userService: UserService) {

    @GetMapping("/{userId}")
    fun findMemberById(@PathVariable("userId") userId: Long) =
            userService.findMemberById(userId)

    @PutMapping
    fun createMember(@RequestBody createMemberRequest: CreateMemberRequest) =
            userService.createNewUser(createMemberRequest)

    @GetMapping("/edit/profile-photo")
    fun editProfilePhoto(changeProfilePhotoRequest: ChangeProfilePhotoRequest) =
            userService.editProfilePhoto(changeProfilePhotoRequest)
}