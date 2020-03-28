import {Component, Inject} from "@angular/core";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {TripResponse} from "../../interfaces/trip-response.interface";

@Component({
	templateUrl: 'trip-details.dialog.html',
	styleUrls: ['trip-details.dialog.scss']
})
export class TripDetailsDialog {
	trip: TripResponse;

	constructor(
		public dialogRef: MatDialogRef<TripDetailsDialog>,
		@Inject(MAT_DIALOG_DATA) public data: TripResponse) {
		this.trip = data;
	}

	onCancel(): void {
		this.dialogRef.close();
	}

}