package com.project.najdiprevoz.security

import com.project.najdiprevoz.exceptions.UserBannedException
import com.project.najdiprevoz.exceptions.UserNotActivatedException
import com.project.najdiprevoz.repositories.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


@Service
class UserDetailsServiceImpl(private val repository: UserRepository) : UserDetailsService {
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = repository.findByUsername(username)
                .orElseThrow { UsernameNotFoundException("User '$username' not found") }
        if (!user.isActivated) {
            throw UserNotActivatedException()
        }
        if (user.isBanned) {
            throw UserBannedException()
        }
        return user
    }
}
