package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.MemberPreferences
import com.project.najdiprevoz.domain.User
import com.project.najdiprevoz.enums.Gender
import com.project.najdiprevoz.exceptions.InvalidUserIdException
import com.project.najdiprevoz.repositories.AuthorityRepository
import com.project.najdiprevoz.repositories.MemberRepository
import com.project.najdiprevoz.web.request.create.CreateMemberRequest
import com.project.najdiprevoz.web.request.edit.ChangeProfilePhotoRequest
import com.project.najdiprevoz.web.response.UserResponse
import org.springframework.context.annotation.Bean
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import java.util.*
import javax.annotation.PostConstruct

@Bean
fun passwordEncoder(): PasswordEncoder {
    return BCryptPasswordEncoder()
}

@Service
class UserService(private val repository: MemberRepository,
                  private val authorityRepository: AuthorityRepository,
                  private val userPreferenceService: UserPreferenceService) {

    fun createNewUser(createMemberRequest: CreateMemberRequest): User {
        val newUser = with(createMemberRequest) {
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
        createDefaultPreferences(newUser)
        return newUser
    }

    fun findUserByUsername(username: String): User =
            repository.findByUsername(username)
                    .orElseThrow { UsernameNotFoundException("User was not found") }


    fun findUserById(userId: Long): User =
            repository.findById(userId)
                    .orElseThrow { InvalidUserIdException(userId) }

    fun editProfilePhoto(req: ChangeProfilePhotoRequest) = with(req) {
        val member = findUserById(userId)
        member.copy(profilePhoto = profilePhoto.toString())
        mapToUserResponse(repository.save(member))
    }

    private fun mapToUserResponse(user: User): UserResponse = with(user) {
        UserResponse(firstName = firstName,
                lastName = lastName,
                profilePhoto = profilePhoto,
                username = user.username,
                phoneNumber = user.phoneNumber
        )
    }

    private fun createDefaultPreferences(user: User) = with(user) {
        userPreferenceService.createMemberPreferences(
                MemberPreferences(isPetAllowed = false, isSmokingAllowed = false, user = this))
    }

    fun changePassword(newPassword: String, username: String) {
        repository.save(findUserByUsername(username)
                .setPassword(passwordEncoder().encode(newPassword)))
    }

//    @PostConstruct
    fun testCreateUser() {
        repository.save(User(
                username = "testuse1r",
                password = "blabla",
                firstName = "blabla",
                lastName = "blabla",
                authority = authorityRepository.findById(1).get(),
                gender = Gender.M,
                phoneNumber = "071711033",
                birthDate = Date.from(ZonedDateTime.now().toInstant())))
    }
}
