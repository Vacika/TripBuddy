import {RideRequest} from "./riderequest.interface";
import {UserShortInfo} from "./user-short-info.interface";

export interface NotificationResponse {
	id: number;
	createdOn: string;
	rideRequest: RideRequest
	type: string;
	actions: string[];
	from: UserShortInfo;
	seen: boolean;
}