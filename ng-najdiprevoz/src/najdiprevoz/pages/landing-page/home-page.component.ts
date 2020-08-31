import {Component} from '@angular/core';
import {Router} from "@angular/router";
import {TripListPage} from "../trip-list/trip-list.page";

@Component({
	templateUrl: './home-page.component.html',
	styleUrls: ['./home-page.component.scss']
})
export class HomePage {

	constructor(private _router: Router) {
	}

	onSearch(formValues: any) {
		this._router.navigate([TripListPage],{queryParams:{
			fromLocation: formValues['fromLocation'],
				toLocation: formValues['toLocation']
			}})
	}
}
