package com.project.najdiprevoz.exceptions

import javassist.NotFoundException

class NotificationNotFoundException(id: Long) : NotFoundException("Notification with ID:[$id] was not found..") {

}
