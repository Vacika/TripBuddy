import {Routes} from "@angular/router";
import {TripListPage} from "../najdiprevoz/pages/trip-list/trip-list.page";
import {TripDetailsPage} from "../najdiprevoz/pages/trip-details/trip-details.page";
import {LoginPage} from "../najdiprevoz/pages/login/login.page";

export const appRoutes: Routes = [
	{path: 'trips/:id', component: TripDetailsPage},
	{path: 'trips', component: TripListPage},
	{path: 'login', component: LoginPage},
	{
		path: '',
		redirectTo: '/trips',
		pathMatch: 'full'
	}];
