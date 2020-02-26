import {UserShortInfo} from "./user-short-info.interface";

export interface RideRequest {
  id: number
  profilePhoto: string
  requester: UserShortInfo
  tripId: number
}
