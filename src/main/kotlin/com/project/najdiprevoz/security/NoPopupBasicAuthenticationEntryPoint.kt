package com.project.najdiprevoz.security

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class NoPopupBasicAuthenticationEntryPoint : AuthenticationEntryPoint {
    @Throws(IOException::class)
    override fun commence(request: HttpServletRequest, response: HttpServletResponse, authException: AuthenticationException) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "ERROR_NOT_AUTHORIZED")
    }
}