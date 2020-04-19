import {Component, OnInit} from "@angular/core";
import {AbstractControl, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {emailRegex} from "../../contants";
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";
import {isNotNullOrUndefined} from "codelyzer/util/isNotNullOrUndefined";
import {User} from "../../interfaces/user.interface";
import {TranslateService} from "@ngx-translate/core";

@Component({
	templateUrl: './login.page.html',
	styleUrls: ['./login.page.scss']
})
export class LoginPage implements OnInit {
	loginForm: FormGroup;


	constructor(private formBuilder: FormBuilder,
							private router: Router,
							private loginService: AuthService,
							private translate: TranslateService) {
	}

	ngOnInit() {
		this.loginForm = this.formBuilder.group({
			email: [null, [Validators.required, Validators.pattern(emailRegex)]],
			password: [null, Validators.required]
		});
	}

	submit() {
		if (this.loginForm.valid) {
			this.loginService.login(this.username.value, this.password.value).subscribe((user: User) => {
					if (isNotNullOrUndefined(user)) {
						this.router.navigate(['/trips']);
						this.translate.use(user.defaultLanguage.toLowerCase());
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
