import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {RideRequestFullResponse} from "../../interfaces/ride-request.interface";

@Component({
	selector: 'ride-requests-component',
	templateUrl: './ride-requests.component.html',
	styleUrls: ['./ride-requests.component.scss'],
})
export class RideRequestsComponent implements OnInit {

	@Input() sentRideRequests: RideRequestFullResponse[];
	@Input() receivedRideRequests: RideRequestFullResponse[];
	@Output() onActionTaken = new EventEmitter<any>();
	tableColumnsForSent = [
		"allowedActions",
		"fromLocation",
		"toLocation",
		"departureTime",
		"requestStatus",
		"driverName",
		"tripId",
		"rideStatus"
	];

	tableColumnsForReceived = [
		"allowedActions",
		"requesterName",
		"fromLocation",
		"toLocation",
		"departureTime",
		"requestStatus",
		"tripId",
		"rideStatus"
	];

	ngOnInit(): void {
	}


	takeAction(actionEvent) {
			this.onActionTaken.emit(actionEvent)
	}
}
