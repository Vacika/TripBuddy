import {Component, EventEmitter, HostBinding, Input, OnInit, Output, ViewEncapsulation} from '@angular/core';
import {TripService} from '../../services/trip.service';
import {ActivatedRoute} from '@angular/router';
import {AbstractControl, FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {City} from '../../interfaces/city.interface';
import {CityService} from '../../services/city.service';

@Component({
	selector: 'search-trips-component',
	templateUrl: './search-trips.page.html',
	styleUrls: ['./search-trips.page.scss'],
	encapsulation: ViewEncapsulation.None
})
export class SearchTripsPage implements OnInit {
	@HostBinding('class') classes = 'page';
	@Output() searchFormEmitter = new EventEmitter();
	@Input() textColor: string = 'color-black';
	@Input() displayDate: boolean = true;
	form: FormGroup = this.searchFormDefinition;
	allCities: City[] = [];
	dateNow: Date = new Date();
	minimumDate = new Date();

	constructor(private _service: TripService,
							private _route: ActivatedRoute,
							private _formBuilder: FormBuilder,
							private _cityService: CityService) {

	};

	@Input() set patchFormValues(newValues: any) {
		if (newValues) {
			let newFormControlValues = {
				fromLocation: newValues.fromLocation ? +newValues.fromLocation : null,
				toLocation: newValues.toLocation ? +newValues.toLocation : null,
				requestedSeats: newValues.requestedSeats ? +newValues.requestedSeats : null,
				departureDate: newValues.departureDate ? new Date(newValues.departureDate) : null
			};
			this.form.setValue(newFormControlValues);
		}
	}

	private get searchFormDefinition() {
		return this._formBuilder.group({
			fromLocation: new FormControl('', Validators.required),
			toLocation: new FormControl('', Validators.required),
			departureDate: new FormControl(null),
			requestedSeats: new FormControl(null)
		});
	}

	ngOnInit(): void {
		this._cityService.getAllCities().subscribe(it => this.allCities = it);
		this.minimumDate.setDate(this.dateNow.getDate() - 1); //TODO: Fix this workaround
	}

	submit() {
		let formValuesForEmit = this.form.value;
		var date = new Date(formValuesForEmit['departureDate']);
		if (this.form.value['departureDate']) {
			var fullDate = (date.getMonth() + 1).toString() + '-' + date.getDate().toString() + '-' + date.getFullYear().toString();
			formValuesForEmit['departureDate'] = fullDate;
		}
		this.searchFormEmitter.emit(formValuesForEmit);
	}

	myDateTimeFilter = (d: Date): boolean => {
		return this.minimumDate <= d;
	};

	theTimeNow() {
		return this.dateNow;
	}

	get fromLocation(): AbstractControl {
		return this.form.controls['fromLocation'];
	}

	get toLocation(): AbstractControl {
		return this.form.controls['toLocation'];
	}

	switchLocations() {
		let from = this.fromLocation.value;
		this.fromLocation.patchValue(this.toLocation.value);
		this.toLocation.patchValue(from);
	}

	checkIfFromToAreIdentical(): boolean{
		return (this.fromLocation.value === this.toLocation.value)
	}
}
