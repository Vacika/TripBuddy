import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from "rxjs";

@Injectable({
	providedIn: 'root'
})
export class SmsTripNotificationService {
	readonly path = 'api/public/sms-notifications/new';

	constructor(private _http: HttpClient) {}

	addSmsTripNotification(phone: string, validFor: number, from:string, to: string): Observable<void> {
		const request = {
			phone,
			validFor,
			from,
			to
		}
		return this._http.post<void>(`${this.path}/new`, request);
	}
}