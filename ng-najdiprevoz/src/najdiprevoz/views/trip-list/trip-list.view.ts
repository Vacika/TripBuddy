import {Component, Input, OnInit, EventEmitter, Output} from '@angular/core';
import {TripService} from '../../services/trip.service';
import {TripResponse} from '../../interfaces/trip-response.interface';
import {Router} from '@angular/router';
import {MatDialog} from '@angular/material/dialog';
import {TripDetailsDialog} from '../../dialogs/trip-details-dialog/trip-details.dialog';
import {TripConfirmReservationDialog} from "../../dialogs/trip-confirm-reservation/trip-confirm-reservation.dialog";

@Component({
	selector: 'list-trips',
	templateUrl: './trip-list.view.html',
	styleUrls: ['./trip-list.view.scss']
})
export class TripListView implements OnInit {
	@Input() allTrips: TripResponse[] = [];
	@Input() showSmsMessage: boolean = false;
	@Output() openSmsNotificationEmitter = new EventEmitter<void>();

	constructor(private _service: TripService,
							private _router: Router,
							private _dialog: MatDialog) {
	}

	ngOnInit(): void {
	}

	onClickDetails(tripId: number) {
		const dialogRef = this._dialog.open(TripDetailsDialog, {
			minHeight: '384px',
			width: '500px',
			data: tripId
		});

		dialogRef.componentInstance.reserveEmit.subscribe(trip =>
			this.reserve(trip)
		);

		dialogRef.afterClosed().subscribe(it => console.log('CLOSED'));
	}

	// convertToImage(image) {
	// 	let base64image = btoa(String.fromCharCode.apply(null, new Uint8Array(image)));
	// 	return 'data:image/jpeg;base64,' + image;
	// }

	getNumberAsArray(it: number) {
		let array = [];
		for (let i = 1; i <= it; i++) {
			array.push(i);
		}
		return array;
	}

	getClassForPersonIcon(index: any, availableSeats: number) {
		if (index <= availableSeats) {
			return 'width-21 font-26 color-green pointer ignore-default-color';
		}

		return 'width-21 font-26 color-red pointer ignore-default-color';
	}

	reserve(trip: TripResponse) {
		let data = {
			tripId: trip.id,
			availableSeats: trip.availableSeats
		};
		this._dialog.open(TripConfirmReservationDialog, {
			height: '400px',
			width: '600px',
			data: data
		})
	}

	openSmsNotificationDialog(){
		this.openSmsNotificationEmitter.emit();
	}
}
