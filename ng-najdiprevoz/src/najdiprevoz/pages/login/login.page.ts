import {Component, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {emailRegex} from "../../contants";
import {LoginService} from "../../services/login.service";
import {Router} from "@angular/router";

@Component({
	templateUrl: './login.page.html',
	styleUrls: ['./login.page.scss']
})
export class LoginPage implements OnInit {
	loginForm: FormGroup;


	constructor(private formBuilder: FormBuilder,
							private router: Router,
							private loginService: LoginService) {
	}

	ngOnInit() {
		this.loginForm = this.formBuilder.group({
			email: [null, [Validators.required, Validators.pattern(emailRegex)]],
			password: [null, Validators.required]
		});
	}

	submit() {
		if (this.loginForm.valid) {
			this.loginService.login(this.username, this.password).subscribe(response => {
				if (response == true) {
					this.router.navigate(['/trips'])
				} else {
					alert("Invalid login info..");
				}
			})
		}
		console.log(this.loginForm.value);
	}

	private get username(): string {
		return this.loginForm.controls['email'].value
	}


	private get password(): string {
		return this.loginForm.controls['password'].value
	}
}
