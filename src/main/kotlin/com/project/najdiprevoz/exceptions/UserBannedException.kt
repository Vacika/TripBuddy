package com.project.najdiprevoz.exceptions

import org.springframework.security.core.AuthenticationException

class UserBannedException(s: String) : AuthenticationException("USER_BANNED") {
}
