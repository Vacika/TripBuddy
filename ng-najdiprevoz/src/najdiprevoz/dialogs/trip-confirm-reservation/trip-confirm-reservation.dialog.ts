import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { RideRequestService } from '../../services/ride-request.service';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { UINotificationsService } from '../../services/ui-notifications-service';

@Component({
	templateUrl: 'trip-confirm-reservation.dialog.html',
	styleUrls: ['trip-confirm-reservation.dialog.scss']
})
export class TripConfirmReservationDialog {
	tripId: number;
	availableSeats: number;
	availableOptions = [];
	form = this.formDefinition;

	constructor(public dialogRef: MatDialogRef<TripConfirmReservationDialog>,
							@Inject(MAT_DIALOG_DATA) public data,
							private rideRequestService: RideRequestService,
							private notificationService: UINotificationsService,
							private _formBuilder: FormBuilder) {
		this.availableSeats = data.availableSeats;
		this.tripId = data.tripId;
		this.populateAvailableOptions(data.availableSeats);
	}

	onCancel(): void {
		this.dialogRef.close();
	}

	reserve() {
		this.rideRequestService.newRideRequest(this.tripId, this.getRequestedSeats.value, this.getAdditionalDescription.value)
			.subscribe(() => {
				this.notificationService.success('SUCCESS', 'SUCCESS');
				this.onCancel();
			});
	}

	private get getRequestedSeats() {
		return this.form.controls['requestedSeats'];
	}

	private get getAdditionalDescription() {
		return this.form.controls['additionalDescription'];
	}

	private get formDefinition() {
		return this._formBuilder.group({
			requestedSeats: new FormControl(1, Validators.required),
			additionalDescription: new FormControl(null)
		});
	}

	private populateAvailableOptions(availableSeats: number) {
		for (let i = 1; i <= availableSeats; i++) {
			this.availableOptions[i - 1] = i;
		}
	}
}