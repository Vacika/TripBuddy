import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from "rxjs";
import {TranslateService} from "@ngx-translate/core";

@Injectable({
	providedIn: 'root'
})
export class SmsTripNotificationService {
	readonly path = 'api/public/sms-notifications';

	constructor(private _http: HttpClient,
							private _translateService: TranslateService) {
	}

	addSmsTripNotification(phone: string, validFor: number, from: number, to: number): Observable<void> {
		const lang = this._translateService.currentLang.toUpperCase();
		const request = {
			phone: phone,
			validFor: validFor,
			from: from,
			to: to,
			language: lang
		};
		return this._http.post<void>(`${this.path}/new`, request);
	}
}