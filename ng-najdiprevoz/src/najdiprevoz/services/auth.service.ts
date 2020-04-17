import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {BehaviorSubject, Observable} from "rxjs";
import {map} from "rxjs/operators";
import {User} from "../interfaces/user.interface";
import {isNotNullOrUndefined} from "codelyzer/util/isNotNullOrUndefined";

const apiURI = "/api/login";

@Injectable({
	providedIn: 'root'
})
export class AuthService {
	private currentUser: BehaviorSubject<User | null>;

	constructor(private httpClient: HttpClient) {
		this.currentUser = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('currentUser')));
	}

	public getLoggedUser(): User {
		return this.currentUser.value;
	}

	login(username: string, password: string): Observable<boolean> {
		let params: URLSearchParams = new URLSearchParams();
		let headers = new HttpHeaders()
			.set('Content-Type', 'application/x-www-form-urlencoded');
		params.append("username", username);
		params.append("password", password);
		return this.httpClient.post<any>(apiURI, params.toString(), {headers: headers})
			.pipe(map(user => {
				if (isNotNullOrUndefined(user)) {
					this.currentUser.next(user);
					localStorage.setItem('currentUser', JSON.stringify(user));
					return true;
				}
			}));
	}

	logout() {
		this.currentUser.next(null);
		localStorage.removeItem('currentUser');
		this.httpClient.post('/api/logout', {}).subscribe();
	}
}
