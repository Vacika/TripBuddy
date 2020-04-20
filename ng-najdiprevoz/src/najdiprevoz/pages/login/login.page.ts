import {Component, OnInit} from "@angular/core";
import {AbstractControl, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {emailRegex} from "../../contants";
import {AuthService} from "../../services/auth.service";
import {ActivatedRoute, Router} from "@angular/router";
import {isNotNullOrUndefined} from "codelyzer/util/isNotNullOrUndefined";
import {User} from "../../interfaces/user.interface";
import {TranslateService} from "@ngx-translate/core";

@Component({
	templateUrl: './login.page.html',
	styleUrls: ['./login.page.scss']
})
export class LoginPage implements OnInit {
	loginForm: FormGroup;
	returnUrl = '/trips';

	constructor(private formBuilder: FormBuilder,
							private router: Router,
							private route: ActivatedRoute,
							private loginService: AuthService,
							private translate: TranslateService) {
	}

	ngOnInit() {
		this.route.queryParamMap.subscribe(it => {
			if (isNotNullOrUndefined(it.get("returnUrl"))) {
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
						this.router.navigate([this.returnUrl]);
						this.translate.use(user.defaultLanguage.toLowerCase());
						localStorage.removeItem('lang');
						localStorage.setItem('lang',user.defaultLanguage.toLowerCase());
					}
				},
				() => {
					this.password.reset();
					// this.notificationService.error("INVALID_LOGIN_INFO")
				})
		}
	}

	private get username(): AbstractControl {
		return this.loginForm.controls['email']
	}

	private get password(): AbstractControl {
		return this.loginForm.controls['password']
	}
}
