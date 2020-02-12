package com.project.najdiprevoz.repositories

import com.project.najdiprevoz.domain.MemberPreferences
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberPreferencesRepository : JpaRepository<MemberPreferences, Long> {
}