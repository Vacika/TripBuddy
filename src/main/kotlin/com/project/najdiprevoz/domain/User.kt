package com.project.najdiprevoz.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.project.najdiprevoz.enums.Gender
import com.project.najdiprevoz.web.response.UserProfileResponse
import com.project.najdiprevoz.web.response.UserShortResponse
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false, unique = true)
        val id: Long = 0L,

        @Column(name = "username", nullable = false, unique = true)
        private val username: String,

        @JsonIgnore
        @Column(name = "password", nullable = false)
        private var password: String,

        @Column(name = "first_name", nullable = false)
        var firstName: String,

        @Column(name = "last_name", nullable = false)
        var lastName: String,

        @Column(name = "birth_date", nullable = false)
        var birthDate: Date,

//        @Lob
        @Column(name = "profile_photo", nullable = true)
        var profilePhoto: ByteArray? = null,

        // Owning
        @ManyToOne
        @JsonIgnore
        @JoinColumn(name = "authority_id", nullable = false)
        val authority: Authority,

        @Column(name = "gender", nullable = false)
        @Enumerated(EnumType.STRING)
        val gender: Gender,

        @Column(name = "phone_number", nullable = true)
        var phoneNumber: String? = null,

        @JsonIgnore
        @OneToMany(mappedBy = "ratedUser")
        var ratings: List<Rating> = listOf() //todo:remove this!!!
) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
            Collections.singleton(SimpleGrantedAuthority(authority.authority))

    override fun isEnabled(): Boolean = true

    override fun getUsername(): String = username

    fun getFullName(): String = "$firstName $lastName"

    private fun getAverageRating(): Double = ratings.map { it.rating }.average()

    override fun isCredentialsNonExpired(): Boolean = true

    fun setAuthority(authority: Authority) {
        authority.users.plus(this)
        this.copy(authority = authority) //todo : CHECK THIS if id == null or working good
    }

    override fun getPassword(): String = password

    fun setPassword(password: String) = this.copy(password = password) // TODO: check if this works

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    @Override
    override fun toString(): String = ""

    fun mapToUserShortResponse(): UserShortResponse {
        return UserShortResponse(id = id,
                rating = this.getAverageRating(),
                name = this.getFullName(),
                profilePhoto = this.profilePhoto)
    }

    fun mapToUserProfileResponse(): UserProfileResponse {
        return UserProfileResponse(firstName = firstName,
                lastName = lastName,
                username = username,
                profilePhoto = profilePhoto,
                phoneNumber = phoneNumber,
                gender = gender.name,
                averageRating = getAverageRating(),
                ratings = ratings,
                id = id,
                birthDate = birthDate)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (username != other.username) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + username.hashCode()
        return result
    }
}