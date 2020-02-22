package com.project.najdiprevoz.exceptions

class InvalidUserIdException(id: Long) : IllegalArgumentException("User with ID: [$id] was not found.")