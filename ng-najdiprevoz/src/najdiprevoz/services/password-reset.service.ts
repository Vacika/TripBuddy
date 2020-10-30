import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class PasswordResetService {
	readonly path = 'api/reset-password';

	constructor(private _http: HttpClient) {
	}

	isTokenValid(token: string): Observable<boolean> {
		return this._http.get<boolean>(`${this.path}?token=${token}`);
	}

	handlePasswordReset(token: string, password: string): Observable<void> {
		return this._http.post<void>(`${this.path}`, {token, password});
	}
}