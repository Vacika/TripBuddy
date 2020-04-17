import {Component, OnInit} from "@angular/core";
import {AbstractControl, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {emailRegex} from "../../contants";
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";

@Component({
	templateUrl: './login.page.html',
	styleUrls: ['./login.page.scss']
})
export class LoginPage implements OnInit {
	loginForm: FormGroup;


	constructor(private formBuilder: FormBuilder,
							private router: Router,
							private loginService: AuthService) {
	}

	ngOnInit() {
		this.loginForm = this.formBuilder.group({
			email: [null, [Validators.required, Validators.pattern(emailRegex)]],
			password: [null, Validators.required]
		});
	}

	submit() {
		if (this.loginForm.valid) {
			this.loginService.login(this.username.value, this.password.value).subscribe(response => {
				if (response == true) {
					this.router.navigate(['/trips'])
					// this.notificationService.success("SUCCESS_LOGIN")
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
