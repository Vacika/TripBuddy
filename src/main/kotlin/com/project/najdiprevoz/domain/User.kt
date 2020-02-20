package com.project.najdiprevoz.domain

import com.project.najdiprevoz.enums.Gender
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "id", nullable = false, unique = true)
        val id: Long? = null,

        @Column(name = "username", nullable = false, unique = true)
        private val username: String,

        @Column(name = "password", nullable = false)
        private val password: String,

        @Column(name = "first_name", nullable = false)
        val firstName: String,

        @Column(name = "last_name", nullable = false)
        val lastName: String,

        @Column(name = "birth_date", nullable = false)
        val birthDate: Date,

        @Column(name = "profile_photo", nullable = true)
        val profilePhoto: String? = null,

        // Owning
        @ManyToOne
        @JoinColumn(name = "authority_id", nullable = false)
        val authority: Authority,

        @Column(name = "gender", nullable = false)
        @Enumerated(EnumType.STRING)
        val gender: Gender,

        @Column(name = "phone_number", nullable = true)
        val phoneNumber: String? = null
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return Collections.singleton(SimpleGrantedAuthority(authority.authority))
    }

    override fun isEnabled(): Boolean = true

    override fun getUsername(): String = username

    override fun isCredentialsNonExpired(): Boolean = true

    fun setAuthority(authority: Authority) {
        authority.users.plus(this)
        this.copy(authority = authority) //todo : CHECK THIS if id == null or working good
    }

    override fun getPassword(): String = password

    fun setPassword(password: String) = this.copy(password = password) // TODO: check if this works

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true
}