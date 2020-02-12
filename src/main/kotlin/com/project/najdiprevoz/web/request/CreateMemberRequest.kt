package com.project.najdiprevoz.web.request

import com.project.najdiprevoz.enums.Gender
import java.util.*

class CreateMemberRequest(val email: String,
                          val birthDate: Date,
                          val firstName: String,
                          val lastName: String,
                          val password: String,
                          val gender: Gender,
                          val phoneNumber: String)

