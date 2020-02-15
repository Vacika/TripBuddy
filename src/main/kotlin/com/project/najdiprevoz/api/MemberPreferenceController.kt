package com.project.najdiprevoz.api

import com.project.najdiprevoz.services.MemberPreferencesService
import com.project.najdiprevoz.web.request.edit.EditMemberPreferenceRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/preferences")
class MemberPreferenceController(private val service:MemberPreferencesService) {

    @GetMapping("/{memberId}")
    fun getPreferenceByMember(@PathVariable("memberId") memberId: Long) =
            service.getMemberPreferences(memberId)

    @GetMapping("/edit/{memberId}")
    fun editPreferenceByMember(@PathVariable("memberId") memberId: Long, editMemberPreferenceRequest: EditMemberPreferenceRequest) =
            service.EditMemberPreferenceRequest(memberId,editMemberPreferenceRequest)
}