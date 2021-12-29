export interface Rating {
	id: number
	rating: number
	note?: string
	dateSubmitted: string
}


export interface RatingCustomResponse extends Rating {
	// id: number;
	fromFullName: string;
	fromId: number;
	fromProfilePic?: string;
	tripId: number;
	tripFromLocation: string;
	tripToLocation: string;
	tripDate: string;
	// rating: number;
	// note: string;
	// dateSubmitted: string;
}