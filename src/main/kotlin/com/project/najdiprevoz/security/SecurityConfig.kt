package com.project.najdiprevoz.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.project.najdiprevoz.services.passwordEncoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.WebAttributes
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Configuration
@EnableWebSecurity
class SecurityConfig(private val service: UserDetailsServiceImpl,
                     private val objectMapper: ObjectMapper) : WebSecurityConfigurerAdapter() {
    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
//        auth.authenticationProvider(authenticationProvider())
        auth.userDetailsService(service).passwordEncoder(passwordEncoder())
    }

    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(service)
        authProvider.setPasswordEncoder(passwordEncoder())
        return authProvider
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/login").permitAll()
                .antMatchers("/api/forgot-password**").permitAll()
                .antMatchers("/api/reset-password**").permitAll()
                .antMatchers("/api/trips-list/**").permitAll()
                .antMatchers("/api/cities").permitAll()
                .antMatchers("/api/users/register").permitAll()
                .antMatchers("/api/users/activate").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginProcessingUrl("/api/login")
                .successHandler(::loginSuccessHandler)
                .failureHandler(::loginFailureHandler)
                .and()
                .logout()
                .logoutUrl("/api/logout")
                .logoutSuccessHandler(::logoutSuccessHandler)
                .invalidateHttpSession(true)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(NoPopupBasicAuthenticationEntryPoint())
    }

    private fun loginSuccessHandler(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        response.status = HttpStatus.OK.value()
        objectMapper.writeValue(response.writer, authentication.principal)
    }

    private fun loginFailureHandler(request: HttpServletRequest, response: HttpServletResponse, e: AuthenticationException) {
        response.status = HttpStatus.UNAUTHORIZED.value()
        if(e.message == "USER_NOT_ACTIVATED"){
            response.status = HttpStatus.FORBIDDEN.value()
        }
        response.outputStream.println(objectMapper.writeValueAsString(e.message))
    }

    private fun logoutSuccessHandler(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        response.status = HttpStatus.OK.value()
        objectMapper.writeValue(response.writer, "Successfully logged out!!");
    }
}
