import {City} from "./city.interface";
import {RideRequest} from "./riderequest.interface";

export interface Trip {
  id: number,
  createdOn: string,
  fromLocation: City,
  destination: City,
  departureTime: string,
  totalSeatsOffered: number,
  pricePerHead: number,
  additionalDescription?: string
  rideRequests: RideRequest[];
  status: string,
  isSmokingAllowed: boolean
  isPetAllowed: boolean
  hasAirCondition: boolean
  finished: boolean
  availableSeats: number,
  driverFullName: string
}

