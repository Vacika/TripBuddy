import {Component, OnInit} from '@angular/core';
import {TripService} from '../../services/trip.service';
import {TripResponse} from '../../interfaces/trip-response.interface';
import {ActivatedRoute, Router} from '@angular/router';
import {Observable} from 'rxjs';

@Component({
	templateUrl: './trip-list.page.html',
	styleUrls: ['./trip-list.page.scss']
})
export class TripListPage implements OnInit {
	data$ = new Observable<TripResponse[]>();
	formValues: any;

	constructor(private _service: TripService,
							private _route: ActivatedRoute,
							private _router: Router) {
	}

	navigateWithQueryParams(formValue: any) {
		this._router.navigate([], {
			relativeTo: this._route,
			queryParams: formValue
		});
	}

	onSearchEmit(formValue: any) {
		this.formValues = formValue;
		this.data$ = this._service.findAllFiltered(formValue);
	}

	ngOnInit(): void {
		this.data$ = this._service.getAllTripsForToday();// fetch all active rides
		this._route.queryParams.subscribe(params => {
			if (params && params.fromLocation && params.toLocation) {
				this.onSearchEmit(params);
			}
		});
	}
}
