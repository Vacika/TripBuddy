import { Component, OnInit } from '@angular/core';
import { TripResponse } from '../../interfaces/trip-response.interface';
import { TripService } from '../../services/trip.service';
import { Observable } from 'rxjs';
import { UINotificationsService } from '../../services/ui-notifications-service';
import {PageEvent} from "@angular/material/paginator";
import {Page} from "../../interfaces/page.interface";

@Component({
	selector: 'trips-component',
	templateUrl: './trips.component.html',
	styleUrls: ['./trips.component.scss']
})
export class TripsComponent implements OnInit {
	tripsAsDriver$: Observable<Page<TripResponse[]>>;
	tripsAsPassenger$: Observable<TripResponse[]>;

	tableColumnsAsDriver = [
		'from',
		'to',
		'departureTime',
		'totalSeats',
		'pricePerHead',
		'status',
		'allowedActions'
	];

	tableColumnsAsPassenger = [
		'from',
		'to',
		'departureTime',
		'totalSeats',
		'pricePerHead',
		'driver',
		'status'
	];

	constructor(private _service: TripService,
							private _notificationService: UINotificationsService) {}

	ngOnInit(): void {
		this.tripsAsDriver$ = this._service.getMyTripsAsDriverPaginated();
		this.tripsAsPassenger$ = this._service.getMyTripsAsPassenger();
	}

	takeAction(actionEvent) {
		if (actionEvent.action == 'CANCEL_RIDE') {
			this._service.cancelTrip(actionEvent.id).subscribe(() => {
				this._notificationService.success('CANCEL_RIDE_SUCCESS', 'ACTION_SUCCESS');
				this.tripsAsDriver$ = this._service.getMyTripsAsDriverPaginated();
			}, () => {
				this._notificationService.error('CANCEL_RIDE_FAIL', 'ACTION_FAIL');
			});
		}
	}

	pageChangedTripsAsDriver($event: PageEvent) {
		this.tripsAsDriver$ = this._service.getMyTripsAsDriverPaginated($event.pageIndex, $event.pageSize);
	}
}
