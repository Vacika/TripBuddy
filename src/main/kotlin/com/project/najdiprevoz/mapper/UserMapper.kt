package com.project.najdiprevoz.mapper

import com.project.najdiprevoz.domain.User
import com.project.najdiprevoz.repositories.RideRepository
import com.project.najdiprevoz.services.UserService
import com.project.najdiprevoz.web.request.EditUserProfileRequest
import com.project.najdiprevoz.web.request.create.CreateUserRequest
import com.project.najdiprevoz.web.response.UserProfileResponse
import org.springframework.stereotype.Service

@Service
class UserMapper(private val service: UserService, private val tripRepository: RideRepository) {
    fun createNewUser(createUserRequest: CreateUserRequest): User =
            service.createNewUser(createUserRequest)

    fun editUserProfile(req: EditUserProfileRequest, username: String): User =
            service.editUserProfile(req, username)

    fun activateUser(activationToken: String): Boolean =
            service.activateUser(activationToken)

    fun getUserInfo(userId: Long): UserProfileResponse =
            findById(userId)

    fun updatePassword(updatedPassword: String, id: Long) =
            service.updatePassword(updatedPassword, id)

    fun findById(userId: Long): UserProfileResponse =
            mapToUserProfileResponse(service.findUserById(userId))

    private fun getPublishedRidesCount(userId: Long) =
            tripRepository.findAllByDriverId(userId).size

    private fun mapToUserProfileResponse(user: User) = with(user) {
        UserProfileResponse(id = id, firstName = firstName, lastName = lastName,
                profilePhoto = profilePhoto, username = username, phoneNumber = phoneNumber,
                gender = gender.gender, birthDate = birthDate, ratings = ratings,
                averageRating = getAverageRating(), defaultLanguage = defaultLanguage.longName,
                publishedRides = getPublishedRidesCount(user.id), memberSince = registeredOn)

    }
}