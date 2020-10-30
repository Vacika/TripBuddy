import {Rating} from "./rating.interface";

export interface ReservationRequest {
	id: number,
	createdOn: string,
	rating: Rating,
	status: string
	requesterFullName: string,
	tripId: number;
}
