package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.Authority
import com.project.najdiprevoz.domain.MemberPreferences
import com.project.najdiprevoz.domain.User
import com.project.najdiprevoz.exceptions.InvalidUserIdException
import com.project.najdiprevoz.repositories.MemberRepository
import com.project.najdiprevoz.web.request.create.CreateMemberRequest
import com.project.najdiprevoz.web.request.edit.ChangeProfilePhotoRequest
import com.project.najdiprevoz.web.response.MemberResponse
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Bean
fun passwordEncoder(): PasswordEncoder {
    return BCryptPasswordEncoder()
}

@Service
class UserService(private val repository: MemberRepository,
                  private val userPreferenceService: UserPreferenceService) {

    fun createNewUser(createMemberRequest: CreateMemberRequest): User {
        val newMember = with(createMemberRequest) {
            repository.save(User(
                    firstName = firstName,
                    lastName = lastName,
                    username = username,
                    password = passwordEncoder().encode(password),
                    birthDate = birthDate,
                    gender = gender,
                    phoneNumber = phoneNumber,
                    profilePhoto = null,
                    authority = Authority()))
        }
        createDefaultPreferences(newMember)
        return newMember
    }


    private fun createDefaultPreferences(user: User) {
        userPreferenceService.createMemberPreferences(
                MemberPreferences(isPetAllowed = false, isSmokingAllowed = false, user = user))
    }

    fun findMemberById(userId: Long): User =
            repository.findById(userId)
                    .orElseThrow { InvalidUserIdException(userId) }

    fun editProfilePhoto(changeProfilePhotoRequest: ChangeProfilePhotoRequest) = with(changeProfilePhotoRequest) {
        val member = findMemberById(userId)
        member.copy(profilePhoto = profilePhoto.toString())
        mapToMemberResponse(repository.save(member))
    }

    private fun mapToMemberResponse(user: User): MemberResponse = with(user) {
        MemberResponse(firstName = firstName,
                lastName = lastName,
                profilePhoto = profilePhoto
        )
    }
}
