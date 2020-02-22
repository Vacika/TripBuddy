package com.project.najdiprevoz.exceptions

import javassist.NotFoundException

class NoCarFoundForUserException(userId: Long) : NotFoundException("No car was found for Member with ID: [$userId]")