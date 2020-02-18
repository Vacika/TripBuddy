package com.project.najdiprevoz.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Configuration
@EnableWebSecurity
class SecurityConfig(service: UserDetailsServiceImpl) : WebSecurityConfigurerAdapter() {
    private val service: UserDetailsServiceImpl = service
    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService<UserDetailsService>(service)
                .passwordEncoder(encoder())
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
                .csrf().disable()
                .httpBasic()
                .authenticationEntryPoint(NoPopupBasicAuthenticationEntryPoint())
                .and()
                .authorizeRequests() //                .antMatchers("/api/auth/principal")
//                    .hasAnyRole("USER", "ADMIN")
//
//                .antMatchers("/api/leases/my")
//                    .hasRole("USER")
//
//                .antMatchers(HttpMethod.POST, "/api/leases")
//                    .hasRole("USER")
//
//                .antMatchers("/api/leases/**")
//                    .hasRole("ADMIN")
                .antMatchers("/api/**")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/api/auth/logout")
                .logoutSuccessHandler { request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication? ->
                    request.session.invalidate()
                    response.status = HttpServletResponse.SC_OK
                }
    }

    @Bean
    fun encoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

}