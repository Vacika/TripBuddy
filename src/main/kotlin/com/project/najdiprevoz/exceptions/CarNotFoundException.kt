package com.project.najdiprevoz.exceptions

class CarNotFoundException(id: Long) : IllegalArgumentException("Car with ID: [$id] was not found.")