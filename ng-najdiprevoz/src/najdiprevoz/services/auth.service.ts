import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {User} from "../interfaces/user.interface";
import {isNotNullOrUndefined} from "codelyzer/util/isNotNullOrUndefined";
import {UserService} from "./user.service";


@Injectable({
	providedIn: 'root'
})
export class AuthService {
	readonly path = '/api/public/authenticate';
	private token = '';

	constructor(private httpClient: HttpClient, private userService: UserService) {}

	login(username: string, password: string): Observable<User | null> {
		const req = {
			username,
			password
		}
		return this.httpClient.post<any>(this.path, req)
			.pipe(map(response => {
				if (isNotNullOrUndefined(response.user)) {
					this.userService.setLoggedUser(response.user);
					this.saveTokenToSessionStorage(response);
				}
				return response.user
			}));
	}

	logout() {
		this.userService.resetUserObservable();
		return this.httpClient.post('/api/logout', {});
	}

	private saveTokenToSessionStorage(response) {
		let tokenStr = "Bearer " + response.token;
		this.token = tokenStr;
		sessionStorage.setItem("token", tokenStr);
	}
}
