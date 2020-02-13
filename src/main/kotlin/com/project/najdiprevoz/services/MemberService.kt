package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.Member
import com.project.najdiprevoz.domain.MemberPreferences
import com.project.najdiprevoz.exceptions.InvalidUserIdException
import com.project.najdiprevoz.repositories.MemberRepository
import com.project.najdiprevoz.web.request.create.CreateMemberRequest
import com.project.najdiprevoz.web.request.edit.EditMemberPreferenceRequest
import org.springframework.stereotype.Service

@Service
class MemberService(private val repository: MemberRepository,
                    private val memberPreferencesService: MemberPreferencesService) {

    fun createNewUser(createMemberRequest: CreateMemberRequest) = with(createMemberRequest) {
        createDefaultPreferences(repository.save(
                Member(firstName = firstName,
                        lastName = lastName,
                        email = email,
                        password = password,
                        birthDate = birthDate,
                        gender = gender,
                        phoneNumber = phoneNumber,
                        profilePhoto = null)))
    }

    fun createDefaultPreferences(member: Member): Member {
        memberPreferencesService.createMemberPreferences(
                MemberPreferences(isPetAllowed = false, isSmokingAllowed = false, member = member))
        return member
    }

    fun EditMemberPreferenceRequest(editMemberPreferenceRequest: EditMemberPreferenceRequest): MemberPreferences = with(editMemberPreferenceRequest) {
        memberPreferencesService.EditMemberPreferenceRequest(
                isSmokingAllowed = isSmokingAllowed,
                isPetAllowed = isPetAllowed,
                member = findMemberById(memberId = memberId))
    }


    fun findMemberById(memberId: Long): Member {
        return repository.findById(memberId)
                .orElseThrow {
                    InvalidUserIdException("User with ID [$memberId] was not found")
                }
    }

}