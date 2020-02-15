package com.project.najdiprevoz.exceptions

class NotEnoughSeatsToDeleteException(rideId: Long, seatsToMinus: Int, availableSeats: Int) : IllegalArgumentException("Ride with id: $rideId does not have enough seats to minus. Available seats: $availableSeats, requested seats to remove: $seatsToMinus") {

}
