import {Component, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {emailRegex} from "../../constants/regex.constants";
import {Router} from "@angular/router";
import {UINotificationsService} from '../../services/util/ui-notifications-service';
import {ERROR_REGISTER} from "../../constants/errors.constants";
import {UserService} from "../../services/user.service";

@Component({
	templateUrl: './register.page.html',
	styleUrls: ['./register.page.scss']
})
export class RegisterPage implements OnInit {
	form: FormGroup;
	minDate: Date = new Date(1960, 1, 1, 0, 0, 0);


	constructor(private _formBuilder: FormBuilder,
							private _router: Router,
							private _notificationService: UINotificationsService,
							private _userService: UserService) {
	}

	ngOnInit() {
		this.form = this._formBuilder.group({
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
			this._userService.registerUser(this.form.value).subscribe(response => {
					this._notificationService.success('SUCCESS_REGISTER');
					this._router.navigate(['/registration-success'])
				},
				(err) => {
					this._notificationService.error(ERROR_REGISTER);
				})
		}
	}
}
