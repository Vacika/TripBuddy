package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.MemberPreferences
import com.project.najdiprevoz.exceptions.InvalidUserIdException
import com.project.najdiprevoz.repositories.MemberPreferencesRepository
import org.springframework.stereotype.Service

@Service
class MemberPreferencesService(private val repository: MemberPreferencesRepository) {

    fun EditMemberPreferenceRequest(memberId: Long, isPetAllowed: Boolean, isSmokingAllowed: Boolean): MemberPreferences {
        val preference = repository.findByMember_Id(memberId)
                .orElseThrow { InvalidUserIdException(memberId) }
                .copy(isPetAllowed = isPetAllowed, isSmokingAllowed = isSmokingAllowed)
        return repository.save(preference)
    }

    fun getMemberPreferences(memberId: Long) =
            repository.findByMember_Id(memberId)
                    .orElseThrow { InvalidUserIdException(memberId) }

    fun createMemberPreferences(memberPreference: MemberPreferences) =
            repository.save(memberPreference)


}