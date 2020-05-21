import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {TripDetailsResponse, TripResponse} from '../interfaces/trip-response.interface';
import {Observable} from 'rxjs';
import {HelperService} from './helper.service';
import {PastTripsResponse} from "../interfaces/past-trips-response.interface";

@Injectable({
	providedIn: 'root'
})
export class TripService {
	readonly path = 'api/trips';
	readonly listpath = 'api/trips-list';

	constructor(private _http: HttpClient,
							private _helper: HelperService) {
	}

	getAllTripsForToday(): Observable<TripResponse[]> {
		return this._http.get<TripResponse[]>(`${this.listpath}`);
	}

	getTripInformation(tripId: number): Observable<TripDetailsResponse> {
		return this._http.get<TripDetailsResponse>(`${this.listpath}/${tripId}/info`);
	}

	addNewTrip(formValues: any) {
		return this._http.put(`${this.path}/add`, formValues);
	}

	cancelTrip(tripId: number):Observable<void>{
		return this._http.get<void>(`${this.path}/cancel/${tripId}`)
	}

	findAllFiltered(value: any): Observable<TripResponse[]> {
		const map = new Map();
		map.set('fromLocation', value['fromLocation']);
		map.set('toLocation', value['toLocation']);
		if (value['departureDate']) {
			var date = new Date(value['departureDate']);
			var fullDate = (date.getMonth() + 1).toString() + '-' + date.getDate().toString() + '-' + date.getFullYear().toString();
			map.set('departureDate', fullDate);
		}
		if (value['requestedSeats']) {
			map.set('requestedSeats', value['requestedSeats']);
		}
		return this._http.get<TripResponse[]>(`${this.listpath}/filter${this._helper.mapToQueryString(map)}`);
	}

	getMyPastTrips(): Observable<PastTripsResponse[]> {
		return this._http.get<PastTripsResponse[]>(`${this.path}/history/passenger/past-trips`)
	}

	findById(tripId:number):Observable<TripResponse> {
		return this._http.get<TripResponse>(`${this.listpath}/${tripId}`)
	}

	getMyTripsAsPassenger(): Observable<TripResponse[]>{
		return this._http.get<TripResponse[]>(`${this.path}/passenger/my`)
	}

	getMyTripsAsDriver(): Observable<TripResponse[]>{
		return this._http.get<TripResponse[]>(`${this.path}/driver/my`)
	}
}
