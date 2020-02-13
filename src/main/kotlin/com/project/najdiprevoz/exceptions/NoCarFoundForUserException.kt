package com.project.najdiprevoz.exceptions

import javassist.NotFoundException

class NoCarFoundForUserException(memberId: Long) : NotFoundException("No car was found for Member with ID: [$memberId]") {
}