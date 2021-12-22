import {Component, EventEmitter, Inject, Output} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {TripDetailsResponse, TripResponse} from '../../interfaces/trip-response.interface';
import {Router} from "@angular/router";
import {TripListService} from "../../services/trip-list.service";

@Component({
	templateUrl: 'trip-details.dialog.html',
	styleUrls: ['trip-details.dialog.scss']
})
export class TripDetailsDialog {
	trip: TripResponse;
	tripDetails: TripDetailsResponse;
	@Output() reserveEmit = new EventEmitter<TripResponse>();

	constructor(public dialogRef: MatDialogRef<TripDetailsDialog>,
							private router: Router,
							@Inject(MAT_DIALOG_DATA) public data,
							private _tripListService: TripListService) {

		this._tripListService.getTripInformation(data).subscribe(response => this.tripDetails = response);
		this._tripListService.findById(data).subscribe(response => this.trip = response);
	}

	onCancel(): void {
		this.dialogRef.close();
	}

	reserve() {
		this.reserveEmit.emit(this.trip);
	}

	navigateToUserPage(id: number) {
		this.dialogRef.close();
		this.router.navigate([`/user/${id}`])
	}
}