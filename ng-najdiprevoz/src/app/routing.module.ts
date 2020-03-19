import { Routes } from '@angular/router';
import { TripListPage } from '../najdiprevoz/pages/trip-list/trip-list.page';
import { TripDetailsPage } from '../najdiprevoz/pages/trip-details/trip-details.page';
import { LoginPage } from '../najdiprevoz/pages/login/login.page';
import { CreateTripPage } from '../najdiprevoz/pages/create-trip/create-trip.page';

export const appRoutes: Routes = [
	{ path: 'trips/new', component: CreateTripPage },
	{ path: 'trips/:id', component: TripDetailsPage },
	{ path: 'trips', component: TripListPage },
	{ path: 'login', component: LoginPage },

	{
		path: '',
		redirectTo: '/trips',
		pathMatch: 'full'
	}];
