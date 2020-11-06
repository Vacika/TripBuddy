import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from "rxjs";

@Injectable({
	providedIn: 'root'
})
export class SmsTripNotificationService {
	readonly path = 'api/public/sms-notifications';

	constructor(private _http: HttpClient) {}

	addSmsTripNotification(phone: string, validFor: number, from:number, to: number): Observable<void> {
		const request = {
			phone:phone,
			validFor:validFor,
			from:from,
			to:to
		}
		return this._http.post<void>(`${this.path}/new`, request);
	}
}