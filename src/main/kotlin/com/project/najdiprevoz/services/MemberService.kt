package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.Member
import com.project.najdiprevoz.domain.MemberPreferences
import com.project.najdiprevoz.exceptions.InvalidUserIdException
import com.project.najdiprevoz.repositories.MemberRepository
import com.project.najdiprevoz.web.request.create.CreateMemberRequest
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
        createDefaultPreferences(newMember);
        return newMember
    }

    fun createDefaultPreferences(member: Member) {
        memberPreferencesService.createMemberPreferences(
                MemberPreferences(isPetAllowed = false, isSmokingAllowed = false, member = member))
    }

//    fun EditMemberPreferenceRequest(editMemberPreferenceRequest: EditMemberPreferenceRequest): MemberPreferences = with(editMemberPreferenceRequest) {
//        memberPreferencesService.EditMemberPreferenceRequest(
//                isSmokingAllowed = isSmokingAllowed,
//                isPetAllowed = isPetAllowed,
//                memberId = memberId)
//    }

    fun findMemberById(memberId: Long): Member =
            repository.findById(memberId)
                    .orElseThrow { InvalidUserIdException(memberId) }
}
