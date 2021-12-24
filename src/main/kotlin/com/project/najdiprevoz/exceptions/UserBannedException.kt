package com.project.najdiprevoz.exceptions

import org.springframework.security.core.AuthenticationException

class UserBannedException() : AuthenticationException("EXCEPTION_USER_BANNED_ERROR") {
}
