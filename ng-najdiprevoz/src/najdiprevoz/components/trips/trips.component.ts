import { Component, OnInit } from '@angular/core';
import { TripResponse } from '../../interfaces/trip-response.interface';
import { TripService } from '../../services/trip.service';
import { Observable } from 'rxjs';

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

	constructor(private _service: TripService) {}

	ngOnInit(): void {
		this.tripsAsDriver$ = this._service.getMyTripsAsDriver();
		this.tripsAsPassenger$ = this._service.getMyTripsAsPassenger();
	}

	takeAction(actionEvent) {
		if (actionEvent.action == 'CANCEL_RIDE') {
			this._service.cancelTrip(actionEvent.id).subscribe();
		}
	}
}
