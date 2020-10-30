import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class PasswordForgotService {
	readonly path = 'api/forgot-password';

	constructor(private _http: HttpClient) {
	}

	createResetTokenForUser(username: string): Observable<void> {
		return this._http.post<void>(`${this.path}`, username);
	}
}