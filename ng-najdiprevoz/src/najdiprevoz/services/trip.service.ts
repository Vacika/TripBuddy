import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {TripResponse} from "../interfaces/trip-response.interface";
import {Observable} from "rxjs";
import {Trip} from "../interfaces/trip.interface";

@Injectable({
  providedIn: 'root',
})
export class TripService {
  readonly path = 'api/trips';

  constructor(private _http: HttpClient) {}

  getAllActiveTripsWithFreeSeats(): Observable<TripResponse[]> {
    return this._http.get<TripResponse[]>(`${this.path}`)
  }

  getTripInformation(tripId: number): Observable<Trip>{
    return this._http.get<Trip>(`${this.path}/${tripId}`)
  }

	addNewTrip(formValues: any) {
		return this._http.put(`${this.path}/add`,formValues)
	}
}
