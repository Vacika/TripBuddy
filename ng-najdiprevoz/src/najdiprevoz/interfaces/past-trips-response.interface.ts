import {UserShortInfo} from "./user-short-info.interface";

export interface PastTripsResponse {
	tripId: number;
	from: string;
	to: string;
	driver: UserShortInfo;
	pricePerHead: number;
	canSubmitRating: boolean;
}