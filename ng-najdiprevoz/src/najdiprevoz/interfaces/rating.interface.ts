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
	rideId: number;
	rideFrom: string;
	rideTo: string;
	rideDate: string;
	// rating: number;
	// note: string;
	// dateSubmitted: string;
}