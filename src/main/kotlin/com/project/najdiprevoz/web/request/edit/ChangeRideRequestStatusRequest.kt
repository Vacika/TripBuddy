package com.project.najdiprevoz.web.request.edit

import com.project.najdiprevoz.enums.RideRequestStatus

class ChangeRideRequestStatusRequest(val requestId: Long,
                                     val previousStatus: RideRequestStatus,
                                     val newStatus: RideRequestStatus)