package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.UserPreferences
import com.project.najdiprevoz.exceptions.InvalidUserIdException
import com.project.najdiprevoz.repositories.UserPreferenceRepository
import com.project.najdiprevoz.web.request.edit.EditMemberPreferenceRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class UserPreferenceService(private val repository: UserPreferenceRepository) {

    val logger: Logger = LoggerFactory.getLogger(UserPreferenceService::class.java)

    fun editUserPreferenceRequest(userId: Long, req: EditMemberPreferenceRequest): UserPreferences =
            with(req) {
                val preference = getUserPreference(userId)
                        .copy(isPetAllowed = isPetAllowed, isSmokingAllowed = isSmokingAllowed)
                logger.info("Editing member preference for member $userId.Preference: $preference")
                repository.save(preference)
            }

    fun getUserPreference(userId: Long): UserPreferences =
            repository.findByUserId(userId)
                    .orElseThrow { InvalidUserIdException(userId) }

    fun createMemberPreferences(userPreference: UserPreferences) =
            repository.save(userPreference)


}