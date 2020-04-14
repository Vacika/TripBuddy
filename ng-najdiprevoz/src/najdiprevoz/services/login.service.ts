import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {BehaviorSubject, Observable, of} from "rxjs";
import {catchError, filter, map, switchMap, tap} from "rxjs/operators";
import {isNullOrUndefined} from 'util';
import {Router} from "@angular/router";
import {StateService} from "./state.service";
import {User} from "../interfaces/user.interface";

const apiURI = "/api/login";

@Injectable({
	providedIn: 'root'
})
export class LoginService {
	private currentUser: BehaviorSubject<User | null> = new BehaviorSubject(null);

	constructor(private httpClient: HttpClient,
							private stateService: StateService,
							private router: Router) {
		this.fetchPrincipalObject().subscribe(user => this.currentUser.next(user),
			() => this.router.navigateByUrl('login'));
	}

	login(username: string, password: string): Observable<boolean> {
		let params: URLSearchParams = new URLSearchParams();
		let headers = new HttpHeaders()
			.set('Content-Type', 'application/x-www-form-urlencoded');
		params.append("username", username);
		params.append("password", password);
		return this.httpClient.post<any>(apiURI, params.toString(), {headers: headers})
			.pipe(
				map(() => true),
				catchError(() => {
					// this.notifyService.error("Operation failed!", "Couldn't log in. Please check your credentials.");
					return of(false);
				})
			)
	}

	fetchUserInfo(username: string, password: string): Observable<User | null> {
		return this.login(username, password)
			.pipe(
				filter((it: boolean) => it),
				switchMap(() => this.fetchPrincipalObject()),
				tap(user => this.currentUser.next(user))
			)
	}

	fetchPrincipalObject(): Observable<User | null> {
		return this.httpClient.get<User>('/api/login/authenticated')
	}

	get currentUserObservable(): Observable<User | null> {
		if (isNullOrUndefined(this.currentUser.value)) {
			return this.fetchPrincipalObject();
		}
		return this.currentUser.asObservable();
	}

	logout(): Observable<any> {
		this.currentUser.next(null);
		return this.httpClient.get('/api/logout')
	}
}
