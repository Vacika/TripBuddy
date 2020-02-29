import {UserShortInfo} from "./user-short-info.interface";

export interface RideRequestResponse {
  id: number
  profilePhoto: string
  requester: UserShortInfo
  tripId: number
}
