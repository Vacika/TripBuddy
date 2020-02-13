package com.project.najdiprevoz.repositories

import com.project.najdiprevoz.domain.MemberPreferences
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MemberPreferencesRepository : JpaRepository<MemberPreferences, Long> {

    fun findByMember_Id(memberId: Long): Optional<MemberPreferences>
}