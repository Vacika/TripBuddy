import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { RatingService } from '../../services/rating.service';

@Component({
	templateUrl: 'submit-rating.dialog.html',
	styleUrls: ['submit-rating.dialog.scss']
})
export class SubmitRatingDialog {
	rideRequestId: number;
	form = this.formDefinition;
	availableOptions = [1, 2, 3, 4, 5];

	constructor(public dialogRef: MatDialogRef<SubmitRatingDialog>,
							@Inject(MAT_DIALOG_DATA) public data: number,
							private ratingService: RatingService,
							private _formBuilder: FormBuilder) {
		this.rideRequestId = data;
	}

	submit() {
		this.ratingService.submit(this.rideRequestId, this.getRating.value, this.getNote.value).subscribe(() => this.onCancel());
	}

	onCancel(): void {
		this.dialogRef.close();
	}

	private get getRating() {
		return this.form.controls['rating'];
	}

	private get getNote() {
		return this.form.controls['note'];
	}

	private get formDefinition() {
		return this._formBuilder.group({
			rating: new FormControl(1, Validators.required),
			note: new FormControl(null)
		});
	}
}