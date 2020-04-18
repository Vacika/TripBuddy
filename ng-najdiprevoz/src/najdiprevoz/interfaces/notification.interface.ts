import {UserShortInfo} from "./user-short-info.interface";

export interface NotificationResponse {
	id: number;
	createdOn: string;
	rideRequestId: number
	type: string;
	actions: string[];
	fromId: number;
	fromName: string;
	seen: boolean;
}