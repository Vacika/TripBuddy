package com.project.najdiprevoz.exceptions

import org.springframework.security.core.AuthenticationException

class UserNotActivatedException() : AuthenticationException("USER_NOT_ACTIVATED")
