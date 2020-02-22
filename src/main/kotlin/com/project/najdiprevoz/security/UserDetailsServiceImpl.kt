package com.project.najdiprevoz.security

import com.project.najdiprevoz.repositories.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


@Service
class UserDetailsServiceImpl(repository: UserRepository) : UserDetailsService {
    private val repository: UserRepository = repository
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        return repository.findByUsername(username)
                .orElseThrow { UsernameNotFoundException("User '$username' not found") }
    }

}