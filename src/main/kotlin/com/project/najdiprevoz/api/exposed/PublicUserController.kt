package com.project.najdiprevoz.api.exposed

import com.project.najdiprevoz.domain.User
import com.project.najdiprevoz.mapper.UserMapper
import com.project.najdiprevoz.web.request.create.CreateUserRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/public/users")
class PublicUserController(private val mapper: UserMapper) {

    @PutMapping("/register")
    fun createUser(@RequestBody request: CreateUserRequest): User =
        mapper.createNewUser(request)

    @GetMapping("/activate")
    fun activateUser(@RequestParam(required = false) activationToken: String): Boolean =
        mapper.activateUser(activationToken)
}