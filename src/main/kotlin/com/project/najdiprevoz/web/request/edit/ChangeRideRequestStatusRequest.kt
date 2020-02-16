package com.project.najdiprevoz.web.request.edit

import com.project.najdiprevoz.enums.RequestStatus

class ChangeRideRequestStatusRequest(val requestId: Long,
                                     val previousStatus: RequestStatus,
                                     val newStatus: RequestStatus)