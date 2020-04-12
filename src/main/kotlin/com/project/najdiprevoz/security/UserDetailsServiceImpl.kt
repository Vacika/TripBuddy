package com.project.najdiprevoz.security

import com.project.najdiprevoz.domain.AppUser
import com.project.najdiprevoz.repositories.UserRepository
import com.project.najdiprevoz.services.passwordEncoder
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


@Service
class UserDetailsServiceImpl(private val repository: UserRepository) : UserDetailsService {
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        return repository.findByUsername(username)
                .orElseThrow { UsernameNotFoundException("User '$username' not found") }
    }

    fun login(username: String, password: String): Boolean {
        return loadUserByUsername(username).password == password
    }

}