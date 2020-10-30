import {User} from "./user.interface";

export interface Car {
	id: number
	brand: string,
	model: string,
	yearOfManufacture: string
	seats: number
	owner: User
}
