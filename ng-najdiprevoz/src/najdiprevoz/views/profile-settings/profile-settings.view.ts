import {Component, EventEmitter, OnInit, Output} from "@angular/core";
import {User} from "../../interfaces/user.interface";
import {AbstractControl, FormBuilder, FormControl, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthService} from "../../services/auth.service";

@Component({
	selector: 'profile-settings',
	templateUrl: './profile-settings.view.html',
	styleUrls: ['./profile-settings.view.scss']
})
export class ProfileSettingsView implements OnInit {
	form = this.formDefinition;
	user: User;
	@Output() submitEvent = new EventEmitter();

	constructor(private formBuilder: FormBuilder,
							private router: Router,
							private loginService: AuthService) {
	}

	ngOnInit() {
		this.user = this.loginService.getLoggedUser();
		this.setProperties(this.user);
		this.form.disable();
	}

	submit() {
		this.submitEvent.emit(this.form.value)
	}

	private get formDefinition() {
		return this.formBuilder.group({
			birthDate: new FormControl(null, Validators.required),
			phoneNumber: new FormControl(null, Validators.required),
			password: new FormControl('empty-password', Validators.required),
			gender: new FormControl(null, Validators.required)
		})
	}

	private setProperties(user: User) {
		console.log(Date.parse(user.birthDate).toString());
		let t = new Date(user.birthDate);
		this.birthDate.setValue(t);
		this.gender.patchValue(user.gender);
		this.phoneNumber.setValue(user.phoneNumber);
	}

	private get birthDate(): AbstractControl {
		return this.form.controls['birthDate']
	}

	private get phoneNumber(): AbstractControl {
		return this.form.controls['phoneNumber']
	}

	private get password(): AbstractControl {
		return this.form.controls['password']
	}

	private get gender(): AbstractControl {
		return this.form.controls['gender']
	}

	toggleEdit() {
		this.form.enabled ? this.form.disable() : this.form.enable();
	}

	onCancelEdit() {
		this.setProperties(this.user);
		this.form.disable();
	}
}