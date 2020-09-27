package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.User
import com.project.najdiprevoz.exceptions.InvalidUserIdException
import com.project.najdiprevoz.repositories.AuthorityRepository
import com.project.najdiprevoz.repositories.RatingRepository
import com.project.najdiprevoz.repositories.RideRepository
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
                  private val ratingRepository: RatingRepository,
                  private val tripRepository: RideRepository,
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
                authority = authorityRepository.findByAuthority("ROLE_USER")!!,
                registeredOn = ZonedDateTime.now(),
                forgetPasswordUUID = null))
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
                authority = authorityRepository.findByAuthority("ROLE_ADMIN")!!,
                registeredOn = ZonedDateTime.now(),
                forgetPasswordUUID = null))
    }

    fun findUserByUsername(username: String): User =
            repository.findByUsername(username)
                    .orElseThrow { UsernameNotFoundException("User was not found") }

    fun findUserByForgetPasswordUUID(uuid: UUID) =
            repository.findByForgetPasswordUUID(uuid)
                    .orElseThrow { UsernameNotFoundException("User with forget PW UUID $uuid was not found") }

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

    fun createForgetPasswordUUID(username: String) {
        val user = findUserByUsername(username)
        if (user.forgetPasswordUUID == null) {
            user.forgetPasswordUUID = UUID.randomUUID()
            repository.save(user)
        }
    }

    fun validateForgetPasswordUUID(token: UUID) {
        val user = findUserByForgetPasswordUUID(token)
        user.forgetPasswordUUID = null
        repository.save(user)
    }

    fun getUserInfo(userId: Long): UserProfileResponse = with(findUserById(userId)) {
        UserProfileResponse(id = id, firstName = firstName, lastName = lastName,
                profilePhoto = profilePhoto, username = username, phoneNumber = phoneNumber,
                gender = gender.gender, birthDate = birthDate, ratings = ratings,
                averageRating = getAverageRating(), defaultLanguage = defaultLanguage.longName,
                publishedRides = tripRepository.findAllByDriverId(userId).size, memberSince = registeredOn)
    }

    fun updatePassword(updatedPassword: String, id: Long) {
        val user = findUserById(id)
        user.password = passwordEncoder().encode(updatedPassword)
        repository.save(user)
    }
}

