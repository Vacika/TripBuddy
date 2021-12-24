package com.project.najdiprevoz.services

import com.project.najdiprevoz.constants.RoleConstants.REGULAR_USER_ROLE
import com.project.najdiprevoz.domain.Mail
import com.project.najdiprevoz.domain.User
import com.project.najdiprevoz.exceptions.ActivationTokenNotFoundException
import com.project.najdiprevoz.exceptions.InvalidUserIdException
import com.project.najdiprevoz.repositories.*
import com.project.najdiprevoz.utils.EmailService
import com.project.najdiprevoz.utils.likeSpecification
import com.project.najdiprevoz.utils.whereTrue
import com.project.najdiprevoz.web.request.EditUserProfileRequest
import com.project.najdiprevoz.web.request.create.CreateUserRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.jpa.domain.Specification
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import java.util.*
import javax.transaction.Transactional

@Service
class UserService(
    private val repository: UserRepository,
    private val authorityRepository: AuthorityRepository,
    private val emailService: EmailService,
    private val passwordEncoder: PasswordEncoder,
    @Value("\${najdiprevoz.signature}") private val signature: String,
    @Value("\${najdiprevoz.official-app-link}") private val officialAppUrl: String
) {

    @Transactional
    fun create(createUserRequest: CreateUserRequest): User = with(createUserRequest) {
        repository.save(
            User(
                firstName = firstName,
                lastName = lastName,
                username = username,
                password = passwordEncoder.encode(password),
                birthDate = birthDate,
                gender = gender,
                phoneNumber = phoneNumber,
                profilePhoto = null,
                authority = authorityRepository.findByAuthority(REGULAR_USER_ROLE),
                registeredOn = ZonedDateTime.now(),
                activationToken = UUID.randomUUID().toString(),
                isActivated = false
            )
        ).apply {
            emailService.sendUserActivationMail(createActivationMail(this))
        }

    }

    private fun createActivationMail(user: User): Mail = with(user) {
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


    fun findById(userId: Long): User =
        repository.findById(userId)
            .orElseThrow { InvalidUserIdException(userId) }

    fun findByActivationToken(activationToken: String): User {
        return repository.findByActivationToken(activationToken)
            .orElseThrow {
                ActivationTokenNotFoundException(activationToken)
            }
    }

    fun findByUsername(username: String): User =
        repository.findByUsername(username)
            .orElseThrow { UsernameNotFoundException("User was not found") }

    fun findAllFiltered(username: String?, phone: String?): List<User> {
        return repository.findAll(createSpecification(username, phone))
    }


    @Transactional
    fun updatePersonalData(req: EditUserProfileRequest, username: String): User = with(req) {
        val user = findByUsername(username)
        user.gender = gender
        user.phoneNumber = phoneNumber
        user.birthDate = birthDate
        user.defaultLanguage = defaultLanguage
        if (!password.isNullOrEmpty()) {
            user.password = passwordEncoder.encode(password)
        }
        if (!profilePhoto.isNullOrEmpty()) {
            user.profilePhoto = profilePhoto
        }
        return repository.save(user)
    }

    @Transactional
    fun updatePassword(updatedPassword: String, id: Long) {
        val user = findById(id)
        user.password = passwordEncoder.encode(updatedPassword)
        repository.save(user)
    }

    @Transactional
    fun activate(activationToken: String): Boolean {
        return try {
            val user = findByActivationToken(activationToken)
            user.isActivated = true
            repository.save(user)
            true
        } catch (e: ActivationTokenNotFoundException) {
            false
        }
    }

    @Transactional
    fun activateWithoutToken(username: String) =
        repository.activateUser(username)

    @Transactional
    fun banUser(username: String) {
        val user = findByUsername(username)
        user.isBanned = true
        repository.save(user)
    }

    @Transactional
    fun unbanUser(username: String) {
        val user = findByUsername(username)
        user.isBanned = false
        repository.save(user)
    }

    @Transactional
    fun changeUserRole(username: String, role: String) {
        repository.updateUserRole(username, authorityRepository.findByAuthority(role))
    }

//    fun findAllPaginated(username: String?, phone: String?, pageable: Pageable): Page<User> {
//        return repository.findAll(createSpecification(username, phone), pageable)
//    }

//    @Transactional
//    fun createAdminUser(createUserRequest: CreateUserRequest): User = with(createUserRequest) {
//        repository.save(User(
//            firstName = firstName,
//            lastName = lastName,
//            username = username,
//            password = passwordEncoder().encode(password),
//            birthDate = birthDate,
//            gender = gender,
//            phoneNumber = phoneNumber,
//            profilePhoto = null,
//            authority = authorityRepository.findByAuthority("ROLE_ADMIN"),
//            registeredOn = ZonedDateTime.now(),
//            activationToken = UUID.randomUUID().toString(),
//            isActivated = false))
//    }

    private inline fun <reified T> evaluateSpecification(
        properties: List<String>, value: T?,
        fn: (List<String>, T) -> Specification<User>
    ) = value?.let {
        fn(properties, value)
    }

    private fun createSpecification(username: String?, phone: String?): Specification<User> {
        return listOfNotNull(
            evaluateSpecification(listOf("username"), username, ::likeSpecification),
            evaluateSpecification(listOf("phoneNumber"), phone, ::likeSpecification)
        )
            .fold(whereTrue()) { first, second ->
                Specification.where(first).and(second)
            }
    }
}

