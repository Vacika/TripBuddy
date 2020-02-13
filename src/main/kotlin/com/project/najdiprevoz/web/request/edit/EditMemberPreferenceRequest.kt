package com.project.najdiprevoz.web.request.edit

class EditMemberPreferenceRequest(
        val isSmokingAllowed: Boolean,
        val isPetAllowed: Boolean,
        val memberId: Long
)
