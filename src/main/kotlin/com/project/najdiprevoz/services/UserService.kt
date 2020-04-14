package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.User
import com.project.najdiprevoz.enums.Gender
import com.project.najdiprevoz.exceptions.InvalidUserIdException
import com.project.najdiprevoz.repositories.AuthorityRepository
import com.project.najdiprevoz.repositories.UserRepository
import com.project.najdiprevoz.web.request.create.CreateUserRequest
import com.project.najdiprevoz.web.request.edit.ChangeProfilePhotoRequest
import com.project.najdiprevoz.web.response.UserProfileResponse
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

    fun findUserByUsername(username: String): User =
            repository.findByUsername(username)
                    .orElseThrow { UsernameNotFoundException("User was not found") }


    fun findUserById(userId: Long): User =
            repository.findById(userId)
                    .orElseThrow { InvalidUserIdException(userId) }

    fun editProfilePhoto(req: ChangeProfilePhotoRequest) = with(req) {
        val member = findUserById(userId)
        member.copy(profilePhoto = profilePhoto)
        mapToUserResponse(repository.save(member)) //TODO: Doesn't work, fix
    }

    private fun mapToUserResponse(user: User): UserProfileResponse = user.mapToUserProfileResponse()

    fun changePassword(newPassword: String, username: String) {
        repository.save(findUserByUsername(username)
                .setPassword(passwordEncoder().encode(newPassword)))
    }


//        @PostConstruct
//    fun testCreateUser() {
//        repository.save(User(
//                username = "test.com",
//                password = passwordEncoder().encode("123456789"),
//                firstName = "blabla",
//                lastName = "blabla",
//                authority = authorityRepository.findById(1).get(),
//                gender = Gender.M,
//                phoneNumber = "071711033",
//                birthDate = Date.from(ZonedDateTime.now().toInstant())) )
//    }
}

