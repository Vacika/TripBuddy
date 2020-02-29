import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {City} from "../interfaces/city.interface";

@Injectable({
  providedIn: 'root'
})
export class CityService {
  readonly path = 'api/cities';

  constructor(private _http: HttpClient) {
  }

  getAllCities(): Observable<City[]> {
    return this._http.get<City[]>(`${this.path}`)
  }
}
