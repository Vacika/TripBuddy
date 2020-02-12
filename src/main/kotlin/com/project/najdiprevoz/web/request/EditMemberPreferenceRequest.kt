package com.project.najdiprevoz.web.request

class EditMemberPreferenceRequest(
        val isSmokingAllowed: Boolean,
        val isPetAllowed: Boolean,
        val memberId: Long
)
