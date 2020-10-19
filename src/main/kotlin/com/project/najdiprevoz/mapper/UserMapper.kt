package com.project.najdiprevoz.mapper

import com.project.najdiprevoz.domain.User
import com.project.najdiprevoz.repositories.RatingRepository
import com.project.najdiprevoz.repositories.RideRepository
import com.project.najdiprevoz.services.UserService
import com.project.najdiprevoz.web.request.EditUserProfileRequest
import com.project.najdiprevoz.web.request.create.CreateUserRequest
import com.project.najdiprevoz.web.response.UserProfileResponse
import org.springframework.stereotype.Service

@Service
class UserMapper(private val service: UserService,
                 private val tripRepository: RideRepository,
                 private val ratingViewRepository: RatingRepository) {
    fun createNewUser(createUserRequest: CreateUserRequest): User =
            service.createNewUser(createUserRequest)

    fun editUserProfile(req: EditUserProfileRequest, username: String): User =
            service.editUserProfile(req, username)

    fun activateUser(activationToken: String): Boolean =
            service.activateUser(activationToken)

    fun getUserInfo(userId: Long): UserProfileResponse =
            findById(userId)

    fun findById(userId: Long): UserProfileResponse =
            mapToUserProfileResponse(service.findUserById(userId))

    private fun getPublishedRidesCount(userId: Long) =
            tripRepository.findAllByDriverId(userId).size

    fun mapToUserProfileResponse(user: User) = with(user) {
        val ratings = ratingViewRepository.findAllByRatedUser_Id(id)
        UserProfileResponse(id = id, firstName = firstName, lastName = lastName,
                profilePhoto = profilePhoto, username = username, phoneNumber = phoneNumber,
                gender = gender.gender, birthDate = birthDate, ratings = ratings,
                averageRating = ratings
                        .map { it.rating }
                        .average(),
                defaultLanguage = defaultLanguage.longName,
                publishedRides = getPublishedRidesCount(user.id), memberSince = registeredOn)

    }
}