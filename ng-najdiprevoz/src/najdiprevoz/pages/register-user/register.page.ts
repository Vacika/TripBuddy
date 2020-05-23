import {Component, OnInit} from "@angular/core";
import {AbstractControl, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {emailRegex} from "../../constants/regex.constants";
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";
import { UINotificationsService } from '../../services/ui-notifications-service';

@Component({
	templateUrl: './register.page.html',
	styleUrls: ['./register.page.scss']
})
export class RegisterPage implements OnInit {
	form: FormGroup;


	constructor(private formBuilder: FormBuilder,
							private router: Router,
							private notificationService: UINotificationsService,
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
			this.loginService.registerUser(this.form.value).subscribe(response =>{
				this.notificationService.success('SUCCESS_REGISTER', 'ACTION_SUCCESS');

			this.router.navigate(['/login'])},
				(err) => {
					this.notificationService.success('ERROR_REGISTER', 'ERROR_ACTION');
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
