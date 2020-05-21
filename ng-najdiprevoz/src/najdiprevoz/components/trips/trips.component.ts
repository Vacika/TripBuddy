import { Component, OnInit } from '@angular/core';
import { TripResponse } from '../../interfaces/trip-response.interface';
import { TripService } from '../../services/trip.service';
import { Observable } from 'rxjs';
import { UINotificationsService } from '../../services/ui-notifications-service';

@Component({
	selector: 'trips-component',
	templateUrl: './trips.component.html',
	styleUrls: ['./trips.component.scss']
})
export class TripsComponent implements OnInit {
	tripsAsDriver$: Observable<TripResponse[]>;
	tripsAsPassenger$: Observable<TripResponse[]>;

	tableColumnsAsDriver = [
		'from',
		'to',
		'departureTime',
		'totalSeats',
		'pricePerHead',
		'driver',
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
		this.tripsAsDriver$ = this._service.getMyTripsAsDriver();
		this.tripsAsPassenger$ = this._service.getMyTripsAsPassenger();
	}

	takeAction(actionEvent) {
		if (actionEvent.action == 'CANCEL_RIDE') {
			this._service.cancelTrip(actionEvent.id).subscribe(() => {
				this._notificationService.success('CANCEL_RIDE_SUCCESS', 'ACTION_SUCCESS');
				this.tripsAsDriver$ = this._service.getMyTripsAsDriver();
			}, () => {
				this._notificationService.error('CANCEL_RIDE_FAIL', 'ACTION_FAIL');
			});
		}
	}
}
