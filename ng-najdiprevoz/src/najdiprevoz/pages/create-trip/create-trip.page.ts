import { Component, OnInit } from '@angular/core';
import { TripService } from '../../services/trip.service';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { City } from '../../interfaces/city.interface';
import { CityService } from '../../services/city.service';
import { Observable } from 'rxjs';

@Component({
	templateUrl: './create-trip.page.html',
	styleUrls: ['./create-trip.page.scss']
})
export class CreateTripPage implements OnInit {
	fromToForm: FormGroup;
	passengerInfoForm: FormGroup;
	preferencesForm: FormGroup;
	departureTime: string; //todo
	allCities: City[] = [];
	currentStep = new Observable<number>();

	constructor(private _service: TripService,
							private _cityService: CityService,
							private _route: ActivatedRoute,
							private formBuilder: FormBuilder) {

	};

	ngOnInit(): void {
		this._cityService.getAllCities().subscribe(cities => this.allCities = cities);
		this.fromToForm = this.formBuilder.group({
			fromLocation: new FormControl('', Validators.required),
			toLocation: new FormControl('', Validators.required)
		});
		this.currentStep = new Observable(observer => observer.next(1));

	}

	private _passengerInfoFormDefinition() {
		return this.formBuilder.group({
			totalSeatsOffered: new FormControl(null, Validators.required),
			pricePerHead: new FormControl(null, Validators.required),
			additionalDescription: new FormControl('')
			//TODO: Add departure time
		});
	}
	private _preferencesFormDefinition() {
		return this.formBuilder.group({
			isSmokingAllowed: new FormControl(false, Validators.required),
			isPetAllowed: new FormControl(false, Validators.required),
			hasAirCondition: new FormControl(false, Validators.required),
			maxTwoBackseat: new FormControl(false, Validators.required),
		});
	}
	submitFromTo() {
		if (this.getFromLocation.value != this.getToLocation.value) {
			this.fromToForm.disable();
			this.passengerInfoForm = this._passengerInfoFormDefinition();
			this.currentStep = new Observable(observer => observer.next(2));
		}
	}

	submitPassengerInfo() {
		this.passengerInfoForm.disable();
		this.preferencesForm = this._preferencesFormDefinition();
		this.currentStep = new Observable(observer => observer.next(3));
	}

	submitPreferences(){
		console.log('test');
		//call trip service
	}


	private get getFromLocation() {
		return this.fromToForm.get('fromLocation');
	}

	private get getToLocation() {
		return this.fromToForm.get('toLocation');
	}
}
