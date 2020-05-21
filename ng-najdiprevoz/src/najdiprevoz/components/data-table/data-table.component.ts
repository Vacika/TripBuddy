import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { columnPrettyName } from '../../constants/columns';
import { RideRequestFullResponse } from '../../interfaces/ride-request.interface';

@Component({
	selector: 'data-table',
	templateUrl: './data-table.component.html',
	styleUrls: ['./data-table.component.scss']
})
export class DataTableComponent implements OnInit {

	@Input() displayedColumns = [
		'fromLocation',
		'toLocation',
		'departureTime',
		'requesterName',
		'requestStatus',
		'driverName',
		'tripId',
		'rideStatus',
		'allowedActions'
	];
	@Input() dataSource: any[] = [];

	@Output() onActionTaken = new EventEmitter<any>();

	prettyName(rawName: string): string {
		return columnPrettyName.get(rawName);
	}

	ngOnInit(): void {
	}

	takeAction(actionName: string, requestId: number) {
		this.onActionTaken.emit({ action: actionName, id: requestId });
	}

	getSpanClass(column: string, element: RideRequestFullResponse, indicator: boolean) {
		let classes = [];
		if (column == 'requestStatus') {

			switch (element.requestStatus) {
				case 'PENDING':
					if (!indicator) {
						classes.push('text-light-blue');
					} else {
						classes.push('background-light-blue');
					}
					break;
				case 'APPROVED':
					if (!indicator) {
						classes.push('text-success');
					} else {
						classes.push('background-success');
					}
					break;
				case 'DENIED':
					if (!indicator) {
						classes.push('text-fail');
					} else {
						classes.push('background-fail');
					}
					break;
				case 'EXPIRED':
					if (!indicator) {
						classes.push('text-warn');
					} else {
						classes.push('background-warn');
					}
					break;
				case 'RIDE_CANCELLED':
					if (!indicator) {
						classes.push('text-fail');
					} else {
						classes.push('background-fail');
					}
					break;
				case 'CANCELLED':
					if (!indicator) {
						classes.push('text-fail');
					} else {
						classes.push('background-fail');
					}
					break;
			}
		}
		return classes;
	}
}
