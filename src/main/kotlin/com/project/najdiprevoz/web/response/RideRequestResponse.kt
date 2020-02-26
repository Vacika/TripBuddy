package com.project.najdiprevoz.web.response

class RideRequestResponse(val id: Long,
                          val profilePhoto: ByteArray?,
                          val requester: UserShortResponse,
                          val tripId: Long)