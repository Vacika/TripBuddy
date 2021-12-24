import {Component, OnInit} from '@angular/core';
import {TripResponse} from '../../interfaces/trip-response.interface';
import {TripService} from '../../services/trip.service';
import {Observable} from 'rxjs';
import {UINotificationsService} from '../../services/util/ui-notifications-service';
import {tableColumnsAsDriver, tableColumnsAsPassenger} from "../../constants/columns.constants";
import {CANCEL_RIDE_ACTION} from "../../constants/actions.constants";

@Component({
	selector: 'trips-component',
	templateUrl: './trips.component.html',
	styleUrls: ['./trips.component.scss']
})
export class TripsComponent implements OnInit {
	tripsAsDriver$: Observable<TripResponse[]>;
	tripsAsPassenger$: Observable<TripResponse[]>;

	constructor(private _service: TripService,
							private _notificationService: UINotificationsService) {
	}

	get tableColumnsAsDriver() {
		return tableColumnsAsDriver;
	}

	get tableColumnsAsPassenger() {
		return tableColumnsAsPassenger;
	}

	ngOnInit(): void {
		this.tripsAsDriver$ = this._service.getMyTripsAsDriver();
		this.tripsAsPassenger$ = this._service.getMyTripsAsPassenger();
	}

	takeAction(actionEvent) {
		if (actionEvent.action === CANCEL_RIDE_ACTION) {
			this._service.cancelTrip(actionEvent.element).subscribe(() => {
				this._notificationService.success('CANCEL_RIDE_SUCCESS');
				this.tripsAsDriver$ = this._service.getMyTripsAsDriver();
			}, () => {
				this._notificationService.error('CANCEL_RIDE_FAIL');
			});
		}
	}
}
