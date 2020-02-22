package com.project.najdiprevoz.web.request.edit

class EditUserPreferenceRequest(
        val isSmokingAllowed: Boolean,
        val isPetAllowed: Boolean,
        val userId: Long
)
