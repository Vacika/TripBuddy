import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { TripResponse } from '../interfaces/trip-response.interface';
import { Observable } from 'rxjs';
import { Trip } from '../interfaces/trip.interface';
import { HelperService } from './helper.service';

@Injectable({
	providedIn: 'root'
})
export class TripService {
	readonly path = 'api/trips';

	constructor(private _http: HttpClient,
							private _helper: HelperService) {}

	getAllActiveTripsWithFreeSeats(): Observable<TripResponse[]> {
		return this._http.get<TripResponse[]>(`${this.path}`);
	}

	getTripInformation(tripId: number): Observable<Trip> {
		return this._http.get<Trip>(`${this.path}/${tripId}`);
	}

	addNewTrip(formValues: any) {
		return this._http.put(`${this.path}/add`, formValues);
	}

	findAllFiltered(value: any): Observable<TripResponse[]> {
		const map = new Map();
		map.set('fromLocation', value['fromLocation']);
		map.set('toLocation', value['toLocation']);
		if (value['departureDate']) {
			map.set('departureDate', value['departureDate']);
		}
		if (value['requestedSeats']) {
			map.set('requestedSeats', value['requestedSeats']);
		}
		return this._http.get<TripResponse[]>(`${this.path}/filter${this._helper.mapToQueryString(map)}`)
	}
}
