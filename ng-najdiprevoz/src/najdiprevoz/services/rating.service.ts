import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {RatingCustomResponse} from "../interfaces/rating.interface";
import {Observable} from "rxjs";

@Injectable({
	providedIn: 'root'
})
export class RatingService {
	readonly path = 'api/ratings';

	constructor(private _http: HttpClient) {
	}

	getMyRatings(): Observable<RatingCustomResponse[]> {
		return this._http.get<RatingCustomResponse[]>(this.path);
	}

}
