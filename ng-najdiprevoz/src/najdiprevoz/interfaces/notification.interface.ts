export interface NotificationResponse {
	id: number;
	createdOn: string;
	reservationRequestId: number
	type: string;
	actions: string[];
	fromId: number;
	fromName: string;
	fromProfilePic?: string;
	seen: boolean;
}