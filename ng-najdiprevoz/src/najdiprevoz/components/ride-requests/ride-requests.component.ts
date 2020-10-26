import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {RideRequestFullResponse} from "../../interfaces/ride-request.interface";
import {tableColumnsAsDriver, tableColumnsForReceived, tableColumnsForSent} from "../../constants/columns.constants";

@Component({
	selector: 'ride-requests-component',
	templateUrl: './ride-requests.component.html',
	styleUrls: ['./ride-requests.component.scss'],
})
export class RideRequestsComponent implements OnInit {

	@Input() sentRideRequests: RideRequestFullResponse[];
	@Input() receivedRideRequests: RideRequestFullResponse[];
	@Output() onActionTaken = new EventEmitter<any>();

	get tableColumnsForSent() { return tableColumnsForSent;}
	get tableColumnsForReceived(){ return tableColumnsForReceived;}

	ngOnInit(): void {
	}


	takeAction(actionEvent) {
			this.onActionTaken.emit(actionEvent)
	}
}
