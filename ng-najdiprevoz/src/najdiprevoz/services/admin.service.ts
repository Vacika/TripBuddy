import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {User} from "../interfaces/user.interface";

const path = "/api/admin";

@Injectable({
	providedIn: 'root'
})
export class AdminService {
	constructor(private http: HttpClient) {
	}

	fetchAllUsers(): Observable<User[]> {
		return this.http.get<User[]>(`${path}/users/filter`);
	}

	banUser(username: string): Observable<void> {
		return this.http.put<void>(`${path}/ban`, username)
	}

	unBanUser(username: string): Observable<void> {
		return this.http.put<void>(`${path}/unban`, username)
	}

	activateUser(username: string): Observable<void> {
		return this.http.put<void>(`${path}/activate`, username)
	}

	changeUserRole(username: string, role: string): Observable<void> {
		const body = {
			username,
			role
		}
		return this.http.post<void>(`${path}/change-role`, body)
	}
}