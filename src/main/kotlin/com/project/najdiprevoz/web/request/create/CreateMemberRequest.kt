package com.project.najdiprevoz.web.request.create

import com.project.najdiprevoz.enums.Gender
import java.util.*

class CreateMemberRequest(val username: String,
                          val birthDate: Date,
                          val firstName: String,
                          val lastName: String,
                          val password: String,
                          val gender: Gender,
                          val phoneNumber: String)

