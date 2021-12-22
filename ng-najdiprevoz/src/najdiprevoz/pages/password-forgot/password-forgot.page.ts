import {Component, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {emailRegex} from '../../constants/regex.constants';
import {PasswordForgotService} from '../../services/password-forgot.service';
import {UINotificationsService} from '../../services/util/ui-notifications-service';
import {HomePage} from '../landing-page/home-page.component';
import {Router} from '@angular/router';

@Component({
	templateUrl: './password-forgot.page.html',
	styleUrls: ['./password-forgot.page.scss']
})
export class PasswordForgotPage implements OnInit {
	forgotPasswordForm: FormGroup;

	constructor(private formBuilder: FormBuilder,
							private passwordForgotService: PasswordForgotService,
							private notificationService: UINotificationsService,
							private router: Router) {
	}

	private get username(): AbstractControl {
		return this.forgotPasswordForm.controls['username'];
	}

	ngOnInit() {
		this.forgotPasswordForm = this.formBuilder.group({
			username: [null, [Validators.required, Validators.pattern(emailRegex)]]
		});
	}

	submit() {
		if (this.forgotPasswordForm.valid) {
			this.passwordForgotService.createResetTokenForUser(this.username.value)
				.subscribe(_ => {
						this.notificationService.success('FORGOT_PW_MAIL_SENT');
						this.router.navigate([HomePage]);
					},
					_ => this.notificationService.error('USER_WITH_THAT_MAIL_NOT_FOUND'));
		}
	}
}
