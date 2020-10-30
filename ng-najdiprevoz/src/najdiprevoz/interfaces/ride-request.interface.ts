import {UserShortInfo} from "./user-short-info.interface";

export interface ReservationRequestResponse {
	id: number;
	requester: UserShortInfo
	tripId: number
	allowedActions: string[]
}

export interface ReservationRequestFullResponse {
	id: number
	requesterName: string
	driverName: string
	tripId: number
	allowedActions: string[],
  fromLocation: string,
  toLocation: string,
	departureTime: string,
	requestStatus: string,
	tripStatus: string,
	requestedSeats: string
	additionalDescription?: string
}
