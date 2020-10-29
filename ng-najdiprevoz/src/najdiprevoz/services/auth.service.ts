import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {BehaviorSubject, Observable} from "rxjs";
import {map} from "rxjs/operators";
import {User, UserProfileDetails} from "../interfaces/user.interface";
import {isNotNullOrUndefined} from "codelyzer/util/isNotNullOrUndefined";

const apiURI = "/api/authenticate";

@Injectable({
	providedIn: 'root'
})
export class AuthService {
	private currentUser: BehaviorSubject<User | null>;
	private token: string = "";

	readonly path = "/api/users";

	constructor(private httpClient: HttpClient) {
		this.currentUser = new BehaviorSubject<User>(null);
	}

	public getLoggedUser(): User {
		return this.currentUser.value;
	}

	login(username: string, password: string): Observable<User | null> {
		const req = {
			username,
			password
		}
		// let params: URLSearchParams = new URLSearchParams();
		// let headers = new HttpHeaders()
		// 	.set('Content-Type', 'application/json');
		// params.append("username", username);
		// params.append("password", password);
		return this.httpClient.post<any>(apiURI, req)
			.pipe(map(response => {
				if (isNotNullOrUndefined(response.user)) {
					this.currentUser.next(response.user);
					sessionStorage.setItem('username', JSON.stringify(response.user.username));

					let tokenStr = "Bearer " + response.token;
					this.token = tokenStr;
					sessionStorage.setItem("token", tokenStr);

				}
				return response.user
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
		sessionStorage.removeItem('username');
	}

	editProfile(formValues: any): Observable<User> {
		return this.httpClient.put<User>(`${this.path}/edit`, formValues)
	}

	setLoggedUser(user: User) {
		this.currentUser.next(user);
		sessionStorage.setItem('username', JSON.stringify(user.username));
	}

	getUserDetails(userId: string): Observable<UserProfileDetails> {
		return this.httpClient.get<UserProfileDetails>(`${this.path}/details/${userId}`);
	}

	activateUser(token: string): Observable<boolean> {
		return this.httpClient.get<boolean>(`${this.path}/activate?activationToken=${token}`);
	}

	isUserLoggedIn() {
		let user = sessionStorage.getItem("username");
		console.log(!(user === null));
		return !(user === null);
	}
}
