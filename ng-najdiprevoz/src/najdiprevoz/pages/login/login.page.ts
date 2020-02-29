import {Component, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {emailRegex} from "../../contants";

@Component({
	templateUrl: './login.page.html',
	styleUrls: ['./login.page.scss']
})
export class LoginPage implements OnInit {
	loginForm: FormGroup;


	constructor(private formBuilder: FormBuilder) {
	}

	ngOnInit() {
		this.loginForm = this.formBuilder.group({
			email: [null, [Validators.required, Validators.pattern(emailRegex)]],
			password: [null, Validators.required]
		});
	}

	submit() {
		if (!this.loginForm.valid) {
			return;
		}
		console.log(this.loginForm.value);
	}
}
