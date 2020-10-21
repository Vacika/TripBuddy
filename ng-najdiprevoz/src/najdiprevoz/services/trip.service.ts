import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {TripDetailsResponse, TripResponse} from '../interfaces/trip-response.interface';
import {Observable} from 'rxjs';
import {HelperService} from './helper.service';
import {Page} from "../interfaces/page.interface";

@Injectable({
	providedIn: 'root'
})
export class TripService {
	readonly path = 'api/trips';
	readonly listpath = 'api/trips-list';

	constructor(private _http: HttpClient,
							private _helper: HelperService) {
	}

	findById(tripId: number): Observable<TripResponse> {
		return this._http.get<TripResponse>(`${this.listpath}/${tripId}`);
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

	cancelTrip(tripId: number): Observable<void> {
		return this._http.get<void>(`${this.path}/cancel/${tripId}`);
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
		return this._http.get<TripResponse[]>(`${this.listpath}/filter${this._helper.mapToQueryString(map)}`);
	}

	getMyTripsAsPassenger(): Observable<TripResponse[]> {
		return this._http.get<TripResponse[]>(`${this.path}/my/passenger`);
	}

	getMyTripsAsDriver(): Observable<TripResponse[]> {
		return this._http.get<TripResponse[]>(`${this.path}/my/driver`);
	}

	getMyTripsAsDriverPaginated(pageIndex: number=1, pageSize: number=15) {
		const map = new Map();
		map.set('page', pageIndex);
		map.set('pageSize', pageSize);
		return this._http.get<Page<TripResponse[]>>(`${this.path}/my/driver${this._helper.mapToQueryString(map)}`)
	}
}
