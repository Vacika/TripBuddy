import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { emailRegex } from '../../constants/regex.constants';
import { TranslateService } from '@ngx-translate/core';
import { PasswordForgotService } from '../../services/password-forgot.service';
import { UINotificationsService } from '../../services/ui-notifications-service';

@Component({
	templateUrl: './password-forgot.page.html',
	styleUrls: ['./password-forgot.page.scss']
})
export class PasswordForgotPage implements OnInit {
	forgotPasswordForm: FormGroup;

	constructor(private formBuilder: FormBuilder,
							private passwordForgotService: PasswordForgotService,
							private notificationService: UINotificationsService,
							private translate: TranslateService) {
	}

	ngOnInit() {
		this.forgotPasswordForm = this.formBuilder.group({
			username: [null, [Validators.required, Validators.pattern(emailRegex)]]
		});
	}

	submit() {
		if (this.forgotPasswordForm.valid) {
			this.passwordForgotService.createResetTokenForUser(this.username.value)
				.subscribe(_ => this.notificationService.success("FORGOT_PW_MAIL_SENT"),
					_ => this.notificationService.error("USER_WITH_THAT_MAIL_NOT_FOUND"))
		}
	}

	private get username(): AbstractControl {
		return this.forgotPasswordForm.controls['username'];
	}
}
