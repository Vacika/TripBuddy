import {Rating} from "./rating.interface";

export interface RideRequest {
  id: number,
  createdOn: string,
  rating: Rating,
  status: string
  requesterFullName: string
}
