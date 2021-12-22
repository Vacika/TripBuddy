import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {TripDetailsResponse, TripResponse} from '../interfaces/trip-response.interface';
import {Observable} from 'rxjs';
import {HelperService} from './util/helper.service';

@Injectable({
	providedIn: 'root'
})
export class TripListService {
	readonly listpath = 'api/public/trips-list';

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
}
