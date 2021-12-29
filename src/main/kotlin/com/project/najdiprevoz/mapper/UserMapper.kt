package com.project.najdiprevoz.mapper

import com.project.najdiprevoz.domain.User
import com.project.najdiprevoz.repositories.RatingRepository
import com.project.najdiprevoz.repositories.TripRepository
import com.project.najdiprevoz.services.UserService
import com.project.najdiprevoz.web.request.EditUserProfileRequest
import com.project.najdiprevoz.web.request.create.CreateUserRequest
import com.project.najdiprevoz.web.response.UserProfileResponse
import org.springframework.stereotype.Service

@Service
class UserMapper(private val service: UserService,
                 private val tripRepository: TripRepository,
                 private val ratingViewRepository: RatingRepository) {
    fun createNewUser(createUserRequest: CreateUserRequest): User =
            service.create(createUserRequest)

    fun editUserProfile(req: EditUserProfileRequest, username: String): User =
            service.updatePersonalData(req, username)

    fun activateUser(activationToken: String): Boolean =
            service.activate(activationToken)

    fun getUserInfo(userId: Long): UserProfileResponse =
            findById(userId)

    fun findById(userId: Long): UserProfileResponse =
            mapToUserProfileResponse(service.findById(userId))

    private fun getPublishedTripsCount(userId: Long) =
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
                publishedTrips = getPublishedTripsCount(user.id), memberSince = registeredOn)

    }
}