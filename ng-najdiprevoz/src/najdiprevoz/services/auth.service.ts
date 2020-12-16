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
	readonly path = "/api/users";
	private currentUser: BehaviorSubject<User | null>;
	private token: string = "";

	constructor(private httpClient: HttpClient) {
		this.currentUser = new BehaviorSubject<User>(null);
	}

	public getLoggedUser(): User {
		if(this.isUserLoggedIn()){
			return JSON.parse(sessionStorage.getItem("currentUser")) as User;
		}
		return null
	}

	login(username: string, password: string): Observable<User | null> {
		const req = {
			username,
			password
		};
		return this.httpClient.post<any>(apiURI, req)
			.pipe(map(response => {
				if (isNotNullOrUndefined(response.user)) {
					this.currentUser.next(response.user);
					sessionStorage.setItem('currentUser', JSON.stringify(response.user));

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
		sessionStorage.removeItem('currentUser');
	}

	editProfile(formValues: any): Observable<User> {
		return this.httpClient.put<User>(`${this.path}/edit`, formValues)
	}

	setLoggedUser(user: User) {
		this.currentUser.next(user);
		sessionStorage.setItem('currentUser', JSON.stringify(user));
	}

	getUserDetails(userId: string): Observable<UserProfileDetails> {
		return this.httpClient.get<UserProfileDetails>(`${this.path}/details/${userId}`);
	}

	activateUser(token: string): Observable<boolean> {
		return this.httpClient.get<boolean>(`${this.path}/activate?activationToken=${token}`);
	}

	//TODO: Remove
	isUserLoggedIn() {
		let user = sessionStorage.getItem("currentUser");
		let token = sessionStorage.getItem("token");
		return !(user === null || token === null);
	}
}
