import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {TripResponse} from '../interfaces/trip-response.interface';
import {Observable} from 'rxjs';
import {HelperService} from './util/helper.service';

@Injectable({
	providedIn: 'root'
})
export class TripService {
	readonly path = 'api/trips';

	constructor(private _http: HttpClient,
							private _helper: HelperService) {
	}

	addNewTrip(formValues: any) {
		return this._http.put(`${this.path}/add`, formValues);
	}

	cancelTrip(tripId: number): Observable<void> {
		return this._http.get<void>(`${this.path}/cancel/${tripId}`);
	}

	getMyTripsAsPassenger(): Observable<TripResponse[]> {
		return this._http.get<TripResponse[]>(`${this.path}/my/passenger`);
	}

	getMyTripsAsDriver(): Observable<TripResponse[]> {
		return this._http.get<TripResponse[]>(`${this.path}/my/driver`);
	}
}
