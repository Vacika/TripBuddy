package com.project.najdiprevoz.web.response

class RideRequestResponse(val id: Long,
                          val requester: UserShortResponse,
                          val tripId: Long,
                          val allowedActions: List<String>?)