package com.project.najdiprevoz.exceptions

class InvalidUserIdException(id: Long) : IllegalArgumentException("Member with ID: [$id] was not found.") {}