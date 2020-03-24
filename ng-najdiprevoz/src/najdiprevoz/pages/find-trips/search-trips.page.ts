import { Component, EventEmitter, HostBinding, OnInit, Output } from '@angular/core';
import { TripService } from '../../services/trip.service';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { City } from '../../interfaces/city.interface';
import { CityService } from '../../services/city.service';

@Component({
	selector: 'search-trips-component',
	templateUrl: './search-trips.page.html',
	styleUrls: ['./search-trips.page.scss']
})
export class SearchTripsPage implements OnInit {
	@HostBinding('class') classes = 'page';
	@Output() searchFormEmitter = new EventEmitter();

	form: FormGroup = this.searchFormDefinition;
	allCities: City[] = [];
	departureDateSearched: Date;
	dateNow: Date = new Date();

	constructor(private _service: TripService,
							private _route: ActivatedRoute,
							private _formBuilder: FormBuilder,
							private _cityService: CityService) {

	};

	ngOnInit(): void {
		this._cityService.getAllCities().subscribe(it => this.allCities = it);
		this.dateNow.setDate(this.dateNow.getDate()-1); //TODO: Fix this workaround
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
		this.departureDateSearched = this.form.value['departureDate'];
		this.searchFormEmitter.emit(this.form.value);
	}

	get getDepartureDate() {
		return this.form.controls.departureDate.value;
	}

	getTimeDateNow() {
		return this.dateNow;
	}

	myDateTimeFilter = (d: Date): boolean => {
		return this.dateNow <= d;
	};

	theTimeNow() {
		return new Date();
	}
}
