import {UserShortInfo} from "./user-short-info.interface";

export interface TripResponse {
	id: number
	from: string
	to: string
	departureTime: string
	availableSeats: number,
	totalSeats: number,
	pricePerHead: number
	driver: UserShortInfo;
	maxTwoBackSeat: boolean;
	status: string;
	allowedActions?: string[];
}

export interface TripDetailsResponse {
	isPetAllowed: boolean;
	isSmokingAllowed: boolean;
	hasAirCondition: boolean;
	additionalDescription?: string;
}
