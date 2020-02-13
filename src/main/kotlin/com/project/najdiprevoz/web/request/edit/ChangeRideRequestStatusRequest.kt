package com.project.najdiprevoz.web.request.edit

import com.project.najdiprevoz.enums.RequestRideStatus

class ChangeRideRequestStatusRequest(val requestId: Long,
                                     val status: RequestRideStatus)