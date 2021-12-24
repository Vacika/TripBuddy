package com.project.najdiprevoz.events

import com.project.najdiprevoz.domain.Trip
import org.springframework.context.ApplicationEvent

class TripCancelledEvent(val trip: Trip) : ApplicationEvent(trip)