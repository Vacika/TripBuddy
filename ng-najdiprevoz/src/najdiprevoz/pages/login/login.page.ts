import {Component, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {emailRegex} from '../../constants/regex.constants';
import {AuthService} from '../../services/auth.service';
import {ActivatedRoute, Router} from '@angular/router';
import {isNotNullOrUndefined} from 'codelyzer/util/isNotNullOrUndefined';
import {User} from '../../interfaces/user.interface';
import {TranslateService} from '@ngx-translate/core';
import {UINotificationsService} from '../../services/ui-notifications-service';
import {USER_BANNED_ERROR, USER_NOT_ACTIVATED_ERROR} from "../../constants/errors.constants";

@Component({
	templateUrl: './login.page.html',
	styleUrls: ['./login.page.scss']
})
export class LoginPage implements OnInit {
	loginForm: FormGroup;
	returnUrl = '/trips';

	constructor(private formBuilder: FormBuilder,
							private router: Router,
							private notificationService: UINotificationsService,
							private route: ActivatedRoute,
							private loginService: AuthService,
							private translate: TranslateService) {
	}

	private get username(): AbstractControl {
		return this.loginForm.controls['email'];
	}

	private get password(): AbstractControl {
		return this.loginForm.controls['password'];
	}

	ngOnInit() {
		this.route.queryParamMap.subscribe(it => {
			if (isNotNullOrUndefined(it.get('returnUrl'))) {
				this.returnUrl = it.get('returnUrl');
			}
		});
		this.loginForm = this.formBuilder.group({
			email: [null, [Validators.required, Validators.pattern(emailRegex)]],
			password: [null, Validators.required]
		});
	}

	submit() {
		if (this.loginForm.valid) {
			this.loginService.login(this.username.value, this.password.value).subscribe((user: User) => {
					if (isNotNullOrUndefined(user)) {
						this.notificationService.success('SUCCESS_LOGIN');
						this.router.navigate([this.returnUrl]);
						this.translate.use(user.defaultLanguage.toLowerCase());
						localStorage.removeItem('lang');
						localStorage.setItem('lang', user.defaultLanguage.toLowerCase());
					}
				},
				err => {
					this.password.reset();
					this.notificationService.error(err);
					if (err === USER_NOT_ACTIVATED_ERROR) {
						this.router.navigate(['activation-pending']);
					}
					else if (err === USER_BANNED_ERROR){
						this.router.navigate(['user-banned']);

					}
				});
		}
	}
}
