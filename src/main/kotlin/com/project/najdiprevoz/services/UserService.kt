package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.Mail
import com.project.najdiprevoz.domain.Ride
import com.project.najdiprevoz.domain.User
import com.project.najdiprevoz.exceptions.ActivationTokenNotFoundException
import com.project.najdiprevoz.exceptions.InvalidUserIdException
import com.project.najdiprevoz.repositories.*
import com.project.najdiprevoz.utils.likeSpecification
import com.project.najdiprevoz.utils.whereTrue
import com.project.najdiprevoz.web.request.EditUserProfileRequest
import com.project.najdiprevoz.web.request.create.CreateUserRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.Modifying
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import java.util.*
import javax.transaction.Transactional

@Bean
fun passwordEncoder(): PasswordEncoder {
    return BCryptPasswordEncoder()
}

@Service
class UserService(private val repository: UserRepository,
                  private val authorityRepository: AuthorityRepository,
                  private val emailService: EmailService,
                  @Value("\${najdiprevoz.signature}") private val signature: String,
                  @Value("\${najdiprevoz.official-app-link}") private val officialAppUrl: String) {

    @Transactional
    fun createNewUser(createUserRequest: CreateUserRequest): User = with(createUserRequest) {
        val user = repository.save(User(
                firstName = firstName,
                lastName = lastName,
                username = username,
                password = passwordEncoder().encode(password),
                birthDate = birthDate,
                gender = gender,
                phoneNumber = phoneNumber,
                profilePhoto = null,
                authority = authorityRepository.findByAuthority("ROLE_USER"),
                registeredOn = ZonedDateTime.now(),
                activationToken = UUID.randomUUID().toString(),
                isActivated = false))

        emailService.sendUserActivationMail(createUserActivationMailObject(user))
        user
    }

    private fun createUserActivationMailObject(user: User): Mail = with(user) {
        val mail = Mail()
        mail.lang = defaultLanguage.name
        mail.from = "no-reply@najdiprevoz.com.mk"
        mail.to = username
        mail.subject = "NajdiPrevoz - Activate your user"
        mail.template = "${mail.lang.toLowerCase()}/activation-mail-template"
        val model: MutableMap<String, Any> = HashMap()
        model["user"] = user
        model["signature"] = signature
        model["activationUrl"] = "$officialAppUrl/activate?token=$activationToken"
        mail.model = model
        mail
    }

    @Transactional
    fun createAdminUser(createUserRequest: CreateUserRequest): User = with(createUserRequest) {
        repository.save(User(
                firstName = firstName,
                lastName = lastName,
                username = username,
                password = passwordEncoder().encode(password),
                birthDate = birthDate,
                gender = gender,
                phoneNumber = phoneNumber,
                profilePhoto = null,
                authority = authorityRepository.findByAuthority("ROLE_ADMIN"),
                registeredOn = ZonedDateTime.now(),
                activationToken = UUID.randomUUID().toString(),
                isActivated = false))
    }

    fun findUserByUsername(username: String): User =
            repository.findByUsername(username)
                    .orElseThrow { UsernameNotFoundException("User was not found") }

    fun findUserById(userId: Long): User =
            repository.findById(userId)
                    .orElseThrow { InvalidUserIdException(userId) }

    @Transactional
    fun editUserProfile(req: EditUserProfileRequest, username: String): User = with(req) {
        val user = findUserByUsername(username)
        user.gender = gender
        user.phoneNumber = phoneNumber
        user.birthDate = birthDate
        user.defaultLanguage = defaultLanguage
        if (!password.isNullOrEmpty()) {
            user.password = password
        }
        if (!profilePhoto.isNullOrEmpty()) {
            user.profilePhoto = profilePhoto
        }
        return repository.save(user)
    }

    @Transactional
    fun activateUser(activationToken: String): Boolean {
        return try {
            val user = findUserByActivationToken(activationToken)
            user.isActivated = true
            repository.save(user)
            true
        } catch (e: ActivationTokenNotFoundException) {
            false
        }
    }

    fun findUserByActivationToken(activationToken: String): User {
        return repository.findByActivationToken(activationToken)
                .orElseThrow {
                    ActivationTokenNotFoundException("No user found with activation token $activationToken")
                }
    }

    @Transactional
    fun updatePassword(updatedPassword: String, id: Long) {
        val user = findUserById(id)
        user.password = passwordEncoder().encode(updatedPassword)
        repository.save(user)
    }

    @Transactional
    fun banUser(username: String) {
        val user = findUserByUsername(username)
        user.isBanned = true
        repository.save(user)
    }

    fun isUserBanned(username: String) =
            findUserByUsername(username).isBanned

    fun changeUserRole(username: String, role: String) {
        val user = findUserByUsername(username)
        user.setAuthority(authorityRepository.findByAuthority(role))
        repository.save(user)
    }

    fun unbanUser(username: String) {
        val user = findUserByUsername(username)
        user.isBanned = false
        repository.save(user)
    }

    fun findAllUsersFiltered(username: String?, phone: String?): List<User> {
        return repository.findAll(createSpecification(username, phone))
    }

    //    fun findAllPaginated(username: String?, phone: String?, pageable: Pageable): Page<User> {
//        return repository.findAll(createSpecification(username, phone), pageable)
//    }
    private inline fun <reified T> evaluateSpecification(properties: List<String>, value: T?,
                                                         fn: (List<String>, T) -> Specification<User>) = value?.let {
        fn(properties, value)
    }

    private fun createSpecification(username: String?, phone: String?): Specification<User> {
        return listOfNotNull(evaluateSpecification(listOf("username"), username, ::likeSpecification),
                evaluateSpecification(listOf("phoneNumber"), phone, ::likeSpecification))
                .fold(whereTrue()) { first, second ->
                    Specification.where(first).and(second)
                }
    }

    @Transactional
    fun activateUserWithoutToken(username: String) =
            repository.activateUser(username)
}

