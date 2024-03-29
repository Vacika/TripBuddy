import {Component, EventEmitter, Output} from '@angular/core';
import {User} from '../../interfaces/user.interface';
import {AbstractControl, FormBuilder, FormControl, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {UserService} from "../../services/user.service";

@Component({
	selector: 'profile-settings',
	templateUrl: './my-profile.view.html',
	styleUrls: ['./my-profile.view.scss']
})
export class MyProfileView {
	form = this.formDefinition;
	imgUrl: any;
	user: User;
	@Output() submitEvent = new EventEmitter();

	constructor(private formBuilder: FormBuilder,
							private router: Router,
							private userService: UserService) {
		this.user = this.userService.getLoggedUser();
		if(this.user) {
			this.imgUrl = this.user?.profilePhoto;
			this.setProperties(this.user);
		}
		this.form.disable();
	}

	private get formDefinition() {
		return this.formBuilder.group({
			birthDate: new FormControl(null, Validators.required),
			phoneNumber: new FormControl(null, Validators.required),
			password: new FormControl('empty-password', Validators.required),
			gender: new FormControl(null, Validators.required),
			profilePhoto: new FormControl(null),
			defaultLanguage: new FormControl(null, Validators.required)
		});
	}

	private get defaultLanguage(): AbstractControl {
		return this.form.controls['defaultLanguage'];
	}

	private get profilePhoto(): AbstractControl {
		return this.form.controls['profilePhoto'];
	}

	private get birthDate(): AbstractControl {
		return this.form.controls['birthDate'];
	}

	private get phoneNumber(): AbstractControl {
		return this.form.controls['phoneNumber'];
	}

	private get password(): AbstractControl {
		return this.form.controls['password'];
	}

	private get gender(): AbstractControl {
		return this.form.controls['gender'];
	}

	onFileChange(files) {
		if (files.length === 0) {
			return;
		}

		const mimeType = files[0].type;
		if (mimeType.match(/image\/*/) == null) {
			alert('ONLY IMAGES ALLOWED!'); //TODO: Switch with notify service
			return;
		}
		var reader = new FileReader();
		reader.readAsDataURL(files[0]);
		reader.onload = (_event) => {
			this.imgUrl = reader.result;
			this.profilePhoto.setValue(this.imgUrl);
		};
	}

	submit() {
		if (this.password.value == 'empty-password') {
			this.password.reset();
		}
		this.toggleEdit();
		this.submitEvent.emit(this.form.value);
	}

	toggleEdit() {
		this.form.enabled ? this.form.disable() : this.form.enable();
	}

	onCancelEdit() {
		this.setProperties(this.user);
		this.form.disable();
	}

	openFileBrowser(event: any) {
		event.preventDefault();
		let element: HTMLElement = document.getElementById('profilePhoto') as HTMLElement;
		element.click();
	}

	private setProperties(user: User) {
		console.log(Date.parse(user.birthDate).toString());
		let t = new Date(user.birthDate);
		this.birthDate.setValue(t);
		this.gender.patchValue(user.gender);
		this.phoneNumber.setValue(user.phoneNumber);
		this.profilePhoto.setValue(user.profilePhoto);
		this.defaultLanguage.setValue(user.defaultLanguage);
	}
}