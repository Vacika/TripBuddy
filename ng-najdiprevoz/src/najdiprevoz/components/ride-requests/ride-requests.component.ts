import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ReservationRequestFullResponse} from "../../interfaces/ride-request.interface";
import {tableColumnsForReceived, tableColumnsForSent} from "../../constants/columns.constants";

@Component({
	selector: 'ride-requests-component',
	templateUrl: './ride-requests.component.html',
	styleUrls: ['./ride-requests.component.scss'],
})
export class ReservationRequestsComponent implements OnInit {

	@Input() sentReservationRequests: ReservationRequestFullResponse[];
	@Input() receivedReservationRequests: ReservationRequestFullResponse[];
	@Output() onActionTaken = new EventEmitter<any>();

	get tableColumnsForSent() {
		return tableColumnsForSent;
	}

	get tableColumnsForReceived() {
		return tableColumnsForReceived;
	}

	ngOnInit(): void {
	}


	takeAction(actionEvent) {
		this.onActionTaken.emit(actionEvent)
	}
}
