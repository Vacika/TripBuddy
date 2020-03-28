import { Component, HostBinding, OnInit } from '@angular/core';
import { TripService } from '../../services/trip.service';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { City } from '../../interfaces/city.interface';
import { CityService } from '../../services/city.service';
import { TripResponse } from '../../interfaces/trip-response.interface';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Component({
	templateUrl: './search-trips-page.component.html',
	styleUrls: ['./search-trips-page.component.scss']
})
export class SearchTripsPage implements OnInit {
	@HostBinding('class') classes = 'page';
	form: FormGroup = this.searchFormDefinition;
	allCities: City[] = [];
	dateNow: Date = new Date();
	showTrips: boolean = false;
	data$: Observable<TripResponse[]>;

	constructor(private _service: TripService,
							private _route: ActivatedRoute,
							private _formBuilder: FormBuilder,
							private _cityService: CityService) {

	};

	ngOnInit(): void {
		this._cityService.getAllCities().subscribe(it => this.allCities = it);
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
		this.showTrips = true;
		this.data$ = this._service.findAllFiltered(this.form.value);
	}

	getTimeDateNow() {
		return this.dateNow;
	}

	myDateTimeFilter = (d: Date): boolean => {
		return this.dateNow <= d;
	};
}
