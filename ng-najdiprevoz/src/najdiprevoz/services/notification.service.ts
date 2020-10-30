import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {NotificationResponse} from "../interfaces/notification.interface";

@Injectable({
	providedIn: 'root'
})
export class NotificationService {
	readonly path = 'api/notifications';

	constructor(private _http: HttpClient) {
	}

	fetchUserNotifications(): Observable<NotificationResponse[]> {
		return this._http.get<NotificationResponse[]>(`${this.path}`);
	}

	takeAction(notificationId: number, action: string): Observable<NotificationResponse[]> {
		return this._http.put<NotificationResponse[]>(`${this.path}/${notificationId}/action`, action);
	}
}

