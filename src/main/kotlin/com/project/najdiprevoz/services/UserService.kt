package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.User
import com.project.najdiprevoz.enums.Gender
import com.project.najdiprevoz.enums.Language
import com.project.najdiprevoz.exceptions.InvalidUserIdException
import com.project.najdiprevoz.repositories.AuthorityRepository
import com.project.najdiprevoz.repositories.UserRepository
import com.project.najdiprevoz.web.request.EditUserProfileRequest
import com.project.najdiprevoz.web.request.create.CreateUserRequest
import com.project.najdiprevoz.web.response.UserProfileResponse
import org.springframework.context.annotation.Bean
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import java.util.*

@Bean
fun passwordEncoder(): PasswordEncoder {
    return BCryptPasswordEncoder()
}

@Service
class UserService(private val repository: UserRepository,
                  private val authorityRepository: AuthorityRepository) {
    fun createNewUser(createUserRequest: CreateUserRequest): User = with(createUserRequest) {
        repository.save(User(
                firstName = firstName,
                lastName = lastName,
                username = username,
                password = passwordEncoder().encode(password),
                birthDate = birthDate,
                gender = gender,
                phoneNumber = phoneNumber,
                profilePhoto = null,
                authority = authorityRepository.findByAuthority("ROLE_USER")!!))
    }

    fun createAdminUser(createUserRequest: CreateUserRequest): User = with(createUserRequest) {
        repository.save(User(
                firstName = firstName,
                lastName = lastName,
                username = username,
                password = passwordEncoder().encode(password),
                birthDate = birthDate,
                gender = gender,
                phoneNumber = phoneNumber,
                profilePhoto = null,
                authority = authorityRepository.findByAuthority("ROLE_ADMIN")!!))
    }

    fun findUserByUsername(username: String): User =
            repository.findByUsername(username)
                    .orElseThrow { UsernameNotFoundException("User was not found") }


    fun findUserById(userId: Long): User =
            repository.findById(userId)
                    .orElseThrow { InvalidUserIdException(userId) }

    fun editUserProfile(req: EditUserProfileRequest, username: String): User = with(req) {
        val user = findUserByUsername(username)
        user.gender = gender
        user.phoneNumber = phoneNumber
        user.birthDate = birthDate
        user.defaultLanguage = defaultLanguage
        if (!password.isNullOrEmpty()) {
            user.password = password
        }
        if (!profilePhoto.isNullOrEmpty()) {
            user.profilePhoto = profilePhoto
        }
        return repository.save(user)
    }

    fun getUserInfo(username: String): UserProfileResponse =
        findUserByUsername(username).mapToUserProfileResponse()

}

