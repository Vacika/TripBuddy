import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
	templateUrl: './home-page.component.html',
	styleUrls: ['./home-page.component.scss']
})
export class HomePage {

	constructor(private _router: Router) {
	}

	onSearch(formValues: any) {
		this._router.navigate(['trips'], {
			queryParams: {
				fromLocation: formValues['fromLocation'],
				departureDate: formValues['departureDate'] ? formValues['departureDate'] : null,
				requestedSeats: formValues['requestedSeats'] ? formValues['requestedSeats'] : null,
				toLocation: formValues['toLocation']
			}
		});
	}
}
