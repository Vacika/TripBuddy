import {Rating} from './rating.interface';

//In backend saved as UserProfileResponse
export interface User {
	id: number,
	firstName: string
	lastName: string
	username: string
	profilePhoto?: string
	phoneNumber?: string
	gender: string
	ratings?: Rating[];
	averageRating: number
	birthDate: string
	password?: string
	defaultLanguage: string;
	authorities: { authority:string }[];
}

export interface UserProfileDetails extends User {
	publishedRides: number;
	memberSince: string;
}
