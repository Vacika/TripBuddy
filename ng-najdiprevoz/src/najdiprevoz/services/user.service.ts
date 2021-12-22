import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {BehaviorSubject, Observable} from "rxjs";
import {User, UserProfileDetails} from "../interfaces/user.interface";


@Injectable({
	providedIn: 'root'
})
export class UserService {
	readonly privatePath = "/api/users";
	readonly publicPath = "/api/public/users";
	private currentUser: BehaviorSubject<User | null>;
	private token: string = "";

	constructor(private httpClient: HttpClient) {
		this.currentUser = new BehaviorSubject<User>(null);
	}

	public getLoggedUser(): User {
		if (this.isUserLoggedIn()) {
			return JSON.parse(sessionStorage.getItem("currentUser")) as User;
		}
		return null
	}

	registerUser(formValues): Observable<void> {
		return this.httpClient.put<void>(`${this.publicPath}/register`, formValues)
	}

	resetUserObservable() {
		this.currentUser.next(null);
		sessionStorage.removeItem('currentUser');
	}

	editProfile(formValues: any): Observable<User> {
		return this.httpClient.put<User>(`${this.privatePath}/edit`, formValues)
	}

	setLoggedUser(user: User) {
		this.currentUser.next(user);
		sessionStorage.setItem('currentUser', JSON.stringify(user));
	}

	getUserDetails(userId: string): Observable<UserProfileDetails> {
		return this.httpClient.get<UserProfileDetails>(`${this.privatePath}/details/${userId}`);
	}

	activateUser(token: string): Observable<boolean> {
		return this.httpClient.get<boolean>(`${this.publicPath}/activate?activationToken=${token}`);
	}

	//TODO: Remove
	isUserLoggedIn() {
		let user = sessionStorage.getItem("currentUser");
		let token = sessionStorage.getItem("token");
		return !(user === null || token === null);
	}
}
