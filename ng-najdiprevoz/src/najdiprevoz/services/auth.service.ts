import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {BehaviorSubject, Observable} from "rxjs";
import {map} from "rxjs/operators";
import {User, UserProfileDetails} from "../interfaces/user.interface";
import {isNotNullOrUndefined} from "codelyzer/util/isNotNullOrUndefined";

const apiURI = "/api/login";

@Injectable({
	providedIn: 'root'
})
export class AuthService {
	private currentUser: BehaviorSubject<User | null>;

	readonly path = "/api/users";

	constructor(private httpClient: HttpClient) {
		this.currentUser = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('currentUser')));
	}

	public getLoggedUser(): User {
		return this.currentUser.value;
	}

	login(username: string, password: string): Observable<User | null> {
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
				}
				return user
			}));
	}

	logout() {
		this.resetUserObservable();
		return this.httpClient.post('/api/logout', {});
	}

	registerUser(formValues): Observable<void> {
		return this.httpClient.put<void>(`${this.path}/register`, formValues)
	}

	resetUserObservable() {
		this.currentUser.next(null);
		localStorage.removeItem('currentUser');
	}

	editProfile(formValues: any): Observable<User> {
		return this.httpClient.put<User>(`${this.path}/edit`, formValues)
	}

	setLoggedUser(user: User) {
		this.currentUser.next(user);
		localStorage.setItem('currentUser', JSON.stringify(user));
	}

	getUserDetails(userId: string): Observable<UserProfileDetails> {
		return this.httpClient.get<UserProfileDetails>(`${this.path}/details/user/${userId}`);
	}
}
