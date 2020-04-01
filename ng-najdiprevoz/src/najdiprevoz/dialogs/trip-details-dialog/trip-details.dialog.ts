import {Component, EventEmitter, Inject, Output} from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { TripDetailsResponse, TripResponse } from '../../interfaces/trip-response.interface';
import { RideRequestService } from '../../services/ride-request.service';

@Component({
	templateUrl: 'trip-details.dialog.html',
	styleUrls: ['trip-details.dialog.scss']
})
export class TripDetailsDialog {
	trip: TripResponse;
	tripDetails: TripDetailsResponse;
	@Output() reserveEmit=  new EventEmitter<TripResponse>();

	constructor(public dialogRef: MatDialogRef<TripDetailsDialog>,
							@Inject(MAT_DIALOG_DATA) public data,
							private rideRequestService: RideRequestService) {
		this.trip = data.trip;
		this.tripDetails = data.tripDetails;
	}

	onCancel(): void {
		this.dialogRef.close();
	}

	reserve() {
		this.reserveEmit.emit(this.trip)
	}
}