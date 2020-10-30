import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {ReservationRequestFullResponse} from "../../interfaces/ride-request.interface";

@Component({
	templateUrl: 'reservation-details.dialog.html',
	styleUrls: ['reservation-details.dialog.scss']
})
export class ReservationDetailsDialog {
	reservation: ReservationRequestFullResponse;

	constructor(public dialogRef: MatDialogRef<ReservationDetailsDialog>,
							@Inject(MAT_DIALOG_DATA) public data) {
		this.reservation = data;
	}

	onCancel(): void {
		this.dialogRef.close();
	}
}