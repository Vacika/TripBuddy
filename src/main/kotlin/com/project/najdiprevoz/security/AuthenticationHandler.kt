import org.hibernate.annotations.common.util.impl.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Configuration
class AuthenticationHandler {
    val logger = LoggerFactory.logger(AuthenticationHandler::class.java)

    @Bean
    fun authenticationEntryPoint(): AuthenticationEntryPoint {
        return AuthenticationEntryPoint { request, response, authenticationException ->
            /**
             * Always returns a 401 error code to the client.
             */
            logger.debug("Pre-authenticated entry point called. Rejecting access")
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied")
        }
    }

    @Bean
    fun successHandler(): AuthenticationSuccessHandler {
        return AuthenticationSuccessHandler { request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication? ->
            logger.debug("Authentication success")
            response.status = HttpStatus.OK.value()
            val session = request.session
            if (session != null) {
                session.maxInactiveInterval = 60 * 60 * 10
            }
        }
    }

    @Bean
    fun failureHandler(): AuthenticationFailureHandler {
        return AuthenticationFailureHandler { request: HttpServletRequest?, response: HttpServletResponse, exception: AuthenticationException ->
            logger.debug("Authentication failure", exception)
            response.status = HttpStatus.UNAUTHORIZED.value()
            response.writer.write(exception.message)
        }
    }

    @Bean
    fun logoutSuccessHandler(): LogoutSuccessHandler {
        return LogoutSuccessHandler { request: HttpServletRequest?, response: HttpServletResponse, authentication: Authentication? -> response.status = HttpStatus.OK.value() }
    }
}
