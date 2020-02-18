package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.MemberPreferences
import com.project.najdiprevoz.exceptions.InvalidUserIdException
import com.project.najdiprevoz.repositories.MemberPreferencesRepository
import com.project.najdiprevoz.web.request.edit.EditMemberPreferenceRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class UserPreferenceService(private val repository: MemberPreferencesRepository) {

    val logger: Logger = LoggerFactory.getLogger(UserPreferenceService::class.java)

    fun editUserPreferenceRequest(userId: Long, editMemberPreferenceRequest: EditMemberPreferenceRequest): MemberPreferences =
            with(editMemberPreferenceRequest) {
                val preference = getUserPreference(userId)
                        .copy(isPetAllowed = isPetAllowed, isSmokingAllowed = isSmokingAllowed)
                logger.info("Editing member preference for member $userId.Preference: $preference")
                repository.save(preference)
            }

    fun getUserPreference(userId: Long): MemberPreferences =
            repository.findByUserId(userId)
                    .orElseThrow { InvalidUserIdException(userId) }

    fun createMemberPreferences(memberPreference: MemberPreferences) =
            repository.save(memberPreference)


}