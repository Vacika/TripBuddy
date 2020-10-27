package com.project.najdiprevoz.web.request.edit

import com.project.najdiprevoz.enums.ReservationStatus

class ChangeReservationRequestStatusRequest(val requestId: Long,
                                     val previousStatus: ReservationStatus,
                                     val newStatus: ReservationStatus)