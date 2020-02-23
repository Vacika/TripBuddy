package com.project.najdiprevoz.web.response

class RideRequestResponse(val id: Long,
                          val profilePhoto: String?,
                          val requester: UserResponse,
                          val tripId: Long)