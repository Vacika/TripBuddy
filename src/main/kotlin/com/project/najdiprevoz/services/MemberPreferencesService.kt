package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.Member
import com.project.najdiprevoz.domain.MemberPreferences
import com.project.najdiprevoz.repositories.MemberPreferencesRepository
import org.springframework.stereotype.Service

@Service
class MemberPreferencesService(private val repository: MemberPreferencesRepository) {

    fun EditMemberPreferenceRequest(member: Member, isPetAllowed: Boolean, isSmokingAllowed: Boolean): MemberPreferences {
        val preference = repository.findByMember(member)
                .copy(isPetAllowed = isPetAllowed,
                        isSmokingAllowed = isSmokingAllowed)
        return repository.save(preference)
    }

    fun createMemberPreferences(memberPreference: MemberPreferences): MemberPreferences {
        return repository.save(memberPreference)
    }

}