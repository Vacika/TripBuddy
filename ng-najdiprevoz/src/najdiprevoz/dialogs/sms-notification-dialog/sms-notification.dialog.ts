import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {FormBuilder, FormControl, Validators} from '@angular/forms';
import {UINotificationsService} from '../../services/ui-notifications-service';
import {SmsTripNotificationService} from "../../services/sms-trip-notification.service";
import {mkNumberRegex} from "../../constants/regex.constants";

@Component({
	templateUrl: 'sms-notification.dialog.html',
	styleUrls: ['sms-notification.dialog.scss']
})
export class SmsNotificationDialog implements OnInit {
	dateNow = new Date();

	form = this.formDefinition;
	fromLocation: number;
	toLocation: number;
	constructor(public dialogRef: MatDialogRef<SmsNotificationDialog>,
							@Inject(MAT_DIALOG_DATA) public data: any,
							private _notificationService: UINotificationsService,
							private _service: SmsTripNotificationService,
							private _formBuilder: FormBuilder) {
		this.fromLocation = data.fromLocation;
		this.toLocation = data.toLocation;
	}

	private get getPhone() {
		return this.form.controls['phone'];
	}

	private get validUntil() {
		return this.form.controls['validUntil'];
	}

	private get formDefinition() {
		return this._formBuilder.group({
			phone: new FormControl('', [Validators.required, Validators.maxLength(12), Validators.pattern(mkNumberRegex)]),
			validUntil: new FormControl(null, Validators.required)
		});
	}

	submit() {
		console.log("fromLocation", this.fromLocation);
		this._service.addSmsTripNotification(this.getPhone.value, this.validUntil.value, this.fromLocation, this.toLocation)
			.subscribe(_ => this._notificationService.successAction(),
					_ => this._notificationService.errorAction());
	}

	onCancel(): void {
		this.dialogRef.close();
	}


	myDateTimeFilter = (d: Date): boolean => {
		return this.dateNow <= d;
	};

	getTimeDateNow() {
		return this.dateNow;
	}

	ngOnInit(): void {
		this.form.markAllAsTouched();
	}
}