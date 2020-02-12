package com.project.najdiprevoz.exceptions

import javassist.NotFoundException

class NoCarFoundForUserException(message: String) : NotFoundException(message) {
}