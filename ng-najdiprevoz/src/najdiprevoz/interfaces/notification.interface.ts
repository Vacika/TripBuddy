export interface NotificationResponse {
	id: number;
	createdOn: string;
	rideRequestId: number
	type: string;
	actions: string[];
	fromId: number;
	fromName: string;
	fromProfilePic?: string;
	seen: boolean;
}