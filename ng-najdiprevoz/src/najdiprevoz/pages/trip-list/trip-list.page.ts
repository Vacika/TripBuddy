import {Component, OnInit} from '@angular/core';
import {TripResponse} from '../../interfaces/trip-response.interface';
import {ActivatedRoute, Router} from '@angular/router';
import {Observable} from 'rxjs';
import {SmsNotificationDialog} from "../../dialogs/sms-notification-dialog/sms-notification.dialog";
import {MatDialog} from "@angular/material/dialog";
import {TripListService} from "../../services/trip-list.service";

@Component({
	templateUrl: './trip-list.page.html',
	styleUrls: ['./trip-list.page.scss']
})
export class TripListPage implements OnInit {
	data$ = new Observable<TripResponse[]>();
	formValues: any;
	showSmsMessage = false;
	private fromLocation: string;
	private toLocation: string;

	constructor(private _service: TripListService,
							private _route: ActivatedRoute,
							private _dialog: MatDialog,
							private _router: Router) {
	}

	navigateWithQueryParams(formValue: any) {
		this._router.navigate([], {
			relativeTo: this._route,
			queryParams: formValue
		});
	}

	onSearchEmit(formValue: any) {
		this.formValues = formValue;
		this.data$ = this._service.findAllFiltered(formValue);
	}

	ngOnInit(): void {
		this.data$ = this._service.getAllTripsForToday();// fetch all active rides
		this._route.queryParams.subscribe(params => {
			if (params && params.fromLocation && params.toLocation) {
				this.onSearchEmit(params);
				this.fromLocation = params.fromLocation;
				this.toLocation = params.toLocation;
				this.showSmsMessage = true;
			}
		});
	}

	openSmsDialog() {
		const data = {
			fromLocation: this.fromLocation,
			toLocation: this.toLocation
		}
		this._dialog.open(SmsNotificationDialog,{
			data: data,
			height:'auto',
			width:'auto',
			minWidth:'200px'
		})
	}
}
