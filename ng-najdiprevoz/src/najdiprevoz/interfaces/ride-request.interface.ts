import {UserShortInfo} from "./user-short-info.interface";

export interface RideRequestResponse {
	id: number;
	requester: UserShortInfo
	tripId: number
	allowedActions: string[]
}

export interface RideRequestFullResponse {
	id: number
	requesterName: string
	driverName: string
	tripId: number
	allowedActions: string[],
  fromLocation: string,
  toLocation: string,
	departureTime: string,
	requestStatus: string,
	rideStatus: string
}
