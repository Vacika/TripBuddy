package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.Mail
import com.project.najdiprevoz.domain.User
import com.project.najdiprevoz.exceptions.ActivationTokenNotFoundException
import com.project.najdiprevoz.exceptions.InvalidUserIdException
import com.project.najdiprevoz.repositories.AuthorityRepository
import com.project.najdiprevoz.repositories.RideRepository
import com.project.najdiprevoz.repositories.UserRepository
import com.project.najdiprevoz.web.request.EditUserProfileRequest
import com.project.najdiprevoz.web.request.create.CreateUserRequest
import com.project.najdiprevoz.web.response.UserProfileResponse
import org.springframework.beans.factory.annotation.Value
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
                  private val tripRepository: RideRepository,
                  private val authorityRepository: AuthorityRepository,
                  private val emailService: EmailService,
                  @Value("\${najdiprevoz.signature}") private val signature: String,
                  @Value("\${najdiprevoz.official-app-link}") private val officialAppUrl: String) {

    fun createNewUser(createUserRequest: CreateUserRequest): User = with(createUserRequest) {
        val user = repository.save(User(
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
                activationToken = UUID.randomUUID().toString(),
                isActivated = false))

        emailService.sendUserActivationMail(createUserActivationMailObject(user))
        user
    }

    private fun createUserActivationMailObject(user: User): Mail = with(user){
        val mail = Mail()
        mail.lang = defaultLanguage.name
        mail.from = "no-reply@najdiprevoz.com.mk"
        mail.to = username
        mail.subject = "NajdiPrevoz - Activate your user"
        mail.template = "${mail.lang.toLowerCase()}/activation-mail-template"
        val model: MutableMap<String, Any> = HashMap()
        model["user"] = user
        model["signature"] = signature
        model["activationUrl"] = "$officialAppUrl/activate?token=$activationToken"
        mail.model = model
        mail
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
                activationToken = UUID.randomUUID().toString(),
                isActivated = false))
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

    fun activateUser(activationToken: String): Boolean {
        return try {
            val user = findUserByActivationToken(activationToken)
            user.isActivated = true
            repository.save(user)
            true
        } catch (e: ActivationTokenNotFoundException) {
            false
        }
    }

    fun findUserByActivationToken(activationToken: String): User {
        return repository.findByActivationToken(activationToken)
                .orElseThrow {
                    ActivationTokenNotFoundException("No user found with activation token $activationToken")
                }
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

