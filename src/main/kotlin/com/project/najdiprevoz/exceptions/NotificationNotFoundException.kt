package com.project.najdiprevoz.exceptions

import javassist.NotFoundException

class NotificationNotFoundException(id: Long) : NotFoundException("EXCEPTION_NOTIFICATION_NOT_FOUND") {

}
