import {Component, OnInit} from "@angular/core";
import {AbstractControl, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {emailRegex} from "../../contants";
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";

@Component({
	templateUrl: './register.page.html',
	styleUrls: ['./register.page.scss']
})
export class RegisterPage implements OnInit {
	form: FormGroup;


	constructor(private formBuilder: FormBuilder,
							private router: Router,
							private loginService: AuthService) {
	}

	ngOnInit() {
		this.form = this.formBuilder.group({
			username: [null, [Validators.required, Validators.pattern(emailRegex)]],
			password: [null, Validators.required],
			firstName: [null, Validators.required],
			lastName: [null, Validators.required],
			gender: [null, Validators.required],
			birthDate: [null, Validators.required],
			phoneNumber: [null, Validators.required],
		});
	}

	submit() {
		if (this.form.valid) {
			this.loginService.registerUser(this.form.value).subscribe(response =>
					this.router.navigate(['/login']),
				() => {
					// this.notificationService.error("SOMETHING_WENT_WRONG")
				})
		}
	}

	private get username(): AbstractControl {
		return this.form.controls['username']
	}

	private get password(): AbstractControl {
		return this.form.controls['password']
	}
}
