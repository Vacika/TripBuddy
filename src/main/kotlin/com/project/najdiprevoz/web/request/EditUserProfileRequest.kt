package com.project.najdiprevoz.web.request

import com.project.najdiprevoz.enums.Gender
import com.project.najdiprevoz.enums.Language
import java.util.*

class EditUserProfileRequest(val profilePhoto: String?,
                             val birthDate: Date,
                             val password: String?,
                             val gender: Gender,
                             val phoneNumber: String,
                             val defaultLanguage: Language)
