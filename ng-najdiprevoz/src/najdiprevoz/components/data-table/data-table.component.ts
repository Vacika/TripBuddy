import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {RideRequestFullResponse} from '../../interfaces/ride-request.interface';
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from "@angular/material/sort";
import {columnLabelName} from "../../constants/column-labels";

@Component({
	selector: 'data-table',
	templateUrl: './data-table.component.html',
	styleUrls: ['./data-table.component.scss']
})
export class DataTableComponent implements OnInit {
	dataSource = new MatTableDataSource();
	@ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
	@ViewChild(MatSort) sort: MatSort;

	@Input() set data(data: any[]) {
		if (data) {
			this.dataSource = new MatTableDataSource(data);
			this.dataSource.paginator = this.paginator;
			this.dataSource.sort = this.sort;
		}
	}

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

	@Output() onActionTaken = new EventEmitter<any>();

	getLabel(rawName: string): string {
		return columnLabelName.get(rawName);
	}

	ngOnInit(): void {
	}

	takeAction(actionName: string, element: any) {
		this.onActionTaken.emit({ action: actionName, element: element });
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
