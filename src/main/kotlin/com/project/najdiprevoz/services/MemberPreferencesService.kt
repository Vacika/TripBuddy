package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.MemberPreferences
import com.project.najdiprevoz.exceptions.InvalidUserIdException
import com.project.najdiprevoz.repositories.MemberPreferencesRepository
import com.project.najdiprevoz.web.request.edit.EditMemberPreferenceRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class MemberPreferencesService(private val repository: MemberPreferencesRepository) {

    val logger: Logger = LoggerFactory.getLogger(MemberPreferencesService::class.java)

    fun EditMemberPreferenceRequest(memberId: Long, editMemberPreferenceRequest: EditMemberPreferenceRequest): MemberPreferences =
    with(editMemberPreferenceRequest){
        val preference = repository.findByMemberId(memberId)
                .orElseThrow { InvalidUserIdException(memberId) }
                .copy(isPetAllowed = isPetAllowed, isSmokingAllowed = isSmokingAllowed)
        logger.info("Editing member preference for member $memberId.Preference: $preference")
        repository.save(preference)
    }

    fun getMemberPreferences(memberId: Long): MemberPreferences =
            repository.findByMemberId(memberId)
                    .orElseThrow { InvalidUserIdException(memberId) }

    fun createMemberPreferences(memberPreference: MemberPreferences) =
            repository.save(memberPreference)


}