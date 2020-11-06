import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {FormBuilder, FormControl, Validators} from '@angular/forms';
import {UINotificationsService} from '../../services/ui-notifications-service';
import {SmsTripNotificationService} from "../../services/sms-trip-notification.service";
import {mkNumberRegex, oneDigitRegex} from "../../constants/regex.constants";

@Component({
	templateUrl: 'sms-notification.dialog.html',
	styleUrls: ['sms-notification.dialog.scss']
})
export class SmsNotificationDialog {
	form = this.formDefinition;
	fromLocation: string;
	toLocation: string;
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

	private get getValidFor() {
		return this.form.controls['validFor'];
	}

	private get formDefinition() {
		return this._formBuilder.group({
			phone: new FormControl('', [Validators.required, Validators.maxLength(12), Validators.pattern(mkNumberRegex)]),
			validFor: new FormControl(1, [Validators.required, Validators.pattern(oneDigitRegex)])
		});
	}

	submit() {
		this._service.addSmsTripNotification(this.getPhone.value, this.getValidFor.value, this.fromLocation, this.toLocation)
			.subscribe(_ => this._notificationService.successAction(),
					_ => this._notificationService.errorAction());
	}

	onCancel(): void {
		this.dialogRef.close();
	}
}