import {City} from "./city.interface";
import {ReservationRequest} from "./riderequest.interface";

export interface Trip {
	id: number,
	createdOn: string,
	fromLocation: City,
	destination: City,
	departureTime: string,
	totalSeatsOffered: number,
	pricePerHead: number,
	additionalDescription?: string
	reservationRequests: ReservationRequest[];
	status: string,
	isSmokingAllowed: boolean
	isPetAllowed: boolean
	hasAirCondition: boolean
	finished: boolean
	availableSeats: number,
	driverFullName: string
}

