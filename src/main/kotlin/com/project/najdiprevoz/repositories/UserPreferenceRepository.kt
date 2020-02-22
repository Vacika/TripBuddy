package com.project.najdiprevoz.repositories

import com.project.najdiprevoz.domain.UserPreferences
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserPreferenceRepository : JpaRepository<UserPreferences, Long> {

    fun findByUserId(userId: Long): Optional<UserPreferences>
}