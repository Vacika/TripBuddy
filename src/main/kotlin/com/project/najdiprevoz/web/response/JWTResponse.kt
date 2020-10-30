package com.project.najdiprevoz.web.response

import org.springframework.security.core.userdetails.UserDetails
import java.io.Serializable


class JwtResponse(val token: String, val user: UserDetails) : Serializable {
}