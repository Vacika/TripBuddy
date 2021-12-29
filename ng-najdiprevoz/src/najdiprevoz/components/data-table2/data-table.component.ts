import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from "@angular/material/sort";
import {columnLabelName} from "../../constants/column-labels";
import {DataTableColumn} from "../../interfaces/data-table-column.interface";
import {DataTableColumnType} from "../../interfaces/enums/column-type.enum";

@Component({
	selector: 'data-table',
	templateUrl: './data-table.component.html',
	styleUrls: ['./data-table.component.scss']
})
export class DataTable2Component implements OnInit {
	dataSource: MatTableDataSource<any[]>;
	@ViewChild('paginator', {static: true}) paginator: MatPaginator;
	@ViewChild(MatSort) sort: MatSort;
	ColumnType = DataTableColumnType
	@Input() displayedColumns: DataTableColumn[] = [];
	@Output() onActionTaken = new EventEmitter<any>();

	@Input() set data(data: any[]) {
		if (data) {
			this.dataSource = new MatTableDataSource(data);
			this.dataSource.paginator = this.paginator;
			this.dataSource.sort = this.sort;
		}
	}

	getLabel(rawName: string): string {
		return columnLabelName.get(rawName);
	}

	ngOnInit(): void {
	}

	takeAction(actionName: string, element: any) {
		this.onActionTaken.emit({action: actionName, element: element});
	}

	getSpanClass(status: string, indicator: boolean) {
		let classes = [];
		switch (status) {
			case 'PENDING':
				if (!indicator) {
					classes.push('text-light-blue');
				} else {
					classes.push('background-light-blue inline-block');
				}
				break;

			case 'APPROVED':
				if (!indicator) {
					classes.push('text-success');
				} else {
					classes.push('background-success inline-block');
				}
				break;

			case 'DENIED':
				if (!indicator) {
					classes.push('text-fail');
				} else {
					classes.push('background-fail inline-block');
				}
				break;

			case 'EXPIRED':
				if (!indicator) {
					classes.push('text-warn');
				} else {
					classes.push('background-warn inline-block');
				}
				break;

			case 'FINISHED':
				if (!indicator) {
					classes.push('text-success');
				} else {
					classes.push('background-success inline-block');
				}
				break;
			case 'ACTIVE':
				if (!indicator) {
					classes.push('text-light-blue');
				} else {
					classes.push('background-light-blue inline-block');
				}
				break;

			case 'TRIP_CANCELLED':
				if (!indicator) {
					classes.push('text-fail ');
				} else {
					classes.push('background-fail inline-block');
				}
				break;

			case 'CANCELLED':
				if (!indicator) {
					classes.push('text-fail');
				} else {
					classes.push('background-fail inline-block');
				}
				break;
		}
		return classes;
	}

	getColumnNames(displayedColumns: DataTableColumn[]): string[] {
		return displayedColumns.map(it => it.name);
	}
}
