import { Component, OnInit } from '@angular/core';
import { TripService } from '../../services/trip.service';
import { TripResponse } from '../../interfaces/trip-response.interface';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

@Component({
	templateUrl: './trip-list.page.html',
	styleUrls: ['./trip-list.page.scss']
})
export class TripListPage implements OnInit {
	data$ = new Observable<TripResponse[]>();

	constructor(private _service: TripService,
							private _router: Router) {
	}

	ngOnInit(): void {
		this.data$ = this._service.getAllTripsForToday();// fetch all active rides
	}

	onSearchEmit(formValue: any) {
		this.data$ = this._service.findAllFiltered(formValue);
	}
}
