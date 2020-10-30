import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {HelperService} from './helper.service';
import {Observable} from 'rxjs';
import {ReservationRequestFullResponse} from "../interfaces/ride-request.interface";

@Injectable({
	providedIn: 'root'
})
export class ReservationRequestService {
	readonly path = 'api/reservation-requests';

	constructor(private _http: HttpClient,
							private _helper: HelperService) {
	}

	newReservationRequest(tripId: number, requestedSeats: number, additionalDescription?: string): Observable<void> {
		const request = {
			tripId: tripId,
			requestedSeats: requestedSeats,
			additionalDescription: additionalDescription
		};
		return this._http.put<void>(`${this.path}/new`, request)
	}

	getSentRequests(): Observable<ReservationRequestFullResponse[]> {
		return this._http.get<ReservationRequestFullResponse[]>(`${this.path}/sent`)
	}

	getReceivedRequests(): Observable<ReservationRequestFullResponse[]> {
		return this._http.get<ReservationRequestFullResponse[]>(`${this.path}/received`)
	}

	changeRequestStatus(id: number, action: string): Observable<void> {
		return this._http.get<void>(`${this.path}/${id}/${action.toLowerCase()}`)
	}
}