package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.Member
import com.project.najdiprevoz.domain.MemberPreferences
import com.project.najdiprevoz.exceptions.InvalidUserIdException
import com.project.najdiprevoz.repositories.MemberRepository
import com.project.najdiprevoz.web.request.create.CreateMemberRequest
import com.project.najdiprevoz.web.request.edit.ChangeProfilePhotoRequest
import com.project.najdiprevoz.web.response.MemberResponse
import org.springframework.stereotype.Service

@Service
class MemberService(private val repository: MemberRepository,
                    private val memberPreferencesService: MemberPreferencesService) {

    fun createNewUser(createMemberRequest: CreateMemberRequest): Member {
        val newMember = with(createMemberRequest) {
            repository.save(Member(
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    password = password,
                    birthDate = birthDate,
                    gender = gender,
                    phoneNumber = phoneNumber,
                    profilePhoto = null))
        }
        createDefaultPreferences(newMember)
        return newMember
    }

    private fun createDefaultPreferences(member: Member) {
        memberPreferencesService.createMemberPreferences(
                MemberPreferences(isPetAllowed = false, isSmokingAllowed = false, member = member))
    }

    fun findMemberById(memberId: Long): Member =
            repository.findById(memberId)
                    .orElseThrow { InvalidUserIdException(memberId) }

    fun editProfilePhoto(changeProfilePhotoRequest: ChangeProfilePhotoRequest) = with(changeProfilePhotoRequest) {
        val member = findMemberById(memberId)
        member.copy(profilePhoto = profilePhoto.toString())
        mapToMemberResponse(repository.save(member))
    }

    private fun mapToMemberResponse(member: Member): MemberResponse = with(member) {
        MemberResponse(firstName = firstName,
                lastName = lastName,
                profilePhoto = profilePhoto
        )
    }
}
