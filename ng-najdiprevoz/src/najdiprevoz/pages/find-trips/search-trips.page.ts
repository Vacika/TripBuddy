import {Component, EventEmitter, HostBinding, Input, OnInit, Output, ViewEncapsulation} from '@angular/core';
import {TripService} from '../../services/trip.service';
import {ActivatedRoute} from '@angular/router';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
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

	@Input() set patchFormValues(newValues: any) {
		if (newValues) {
			console.log("NEW VALUES:", newValues);
			let newFormControlValues = {
				fromLocation: newValues.fromLocation ? +newValues.fromLocation : null,
				toLocation: newValues.toLocation ? +newValues.toLocation : null,
				requestedSeats: newValues.requestedSeats ? +newValues.requestedSeats : null,
				departureDate: newValues.departureDate ? new Date(newValues.departureDate) : null
			};
			this.form.setValue(newFormControlValues);
		}
	}

	form: FormGroup = this.searchFormDefinition;
	allCities: City[] = [];
	dateNow: Date = new Date();
	minimumDate = new Date();

	constructor(private _service: TripService,
							private _route: ActivatedRoute,
							private _formBuilder: FormBuilder,
							private _cityService: CityService) {

	};

	ngOnInit(): void {
		this._cityService.getAllCities().subscribe(it => this.allCities = it);
		this.minimumDate.setDate(this.dateNow.getDate() - 1); //TODO: Fix this workaround
	}

	private get searchFormDefinition() {
		return this._formBuilder.group({
			fromLocation: new FormControl('', Validators.required),
			toLocation: new FormControl('', Validators.required),
			departureDate: new FormControl(null),
			requestedSeats: new FormControl(null)
		});
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
}
