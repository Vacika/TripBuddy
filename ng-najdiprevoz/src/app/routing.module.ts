import {Routes} from '@angular/router';
import {TripListPage} from '../najdiprevoz/pages/trip-list/trip-list.page';
import {TripDetailsPage} from '../najdiprevoz/pages/trip-details/trip-details.page';
import {LoginPage} from '../najdiprevoz/pages/login/login.page';
import {CreateTripPage} from '../najdiprevoz/pages/create-trip/create-trip.page';
import {SearchTripsPage} from '../najdiprevoz/pages/find-trips/search-trips.page';
import {NotificationListPage} from "../najdiprevoz/pages/notifications/notifications.page";
import {AuthGuard} from "../najdiprevoz/auth.guard";
import {RegisterPage} from "../najdiprevoz/pages/register-user/register.page";
import {ControlPanelPage} from "../najdiprevoz/pages/control-panel/control-panel.page";
import {UserInfoPage} from "../najdiprevoz/pages/user-info/user-info.page";
import {UserRatingsView} from "../najdiprevoz/views/view-ratings-for-user/my-ratings.view";
import {HomePage} from "../najdiprevoz/pages/landing-page/home-page.component";

export const appRoutes: Routes = [
	{
		path: 'trips/new',
		component: CreateTripPage,
		canActivate: [AuthGuard]
	},
	{
		path: 'trips/search',
		component: SearchTripsPage,
		canActivate: [AuthGuard]
	},
	{
		path: 'trips/:id',
		component: TripDetailsPage
	},
	{
		path: 'trips',
		component: TripListPage
	},
	{
		path: 'login',
		component: LoginPage
	},
	{
		path: 'notifications',
		component: NotificationListPage,
		canActivate: [AuthGuard]
	},
	{
		path: 'register',
		component: RegisterPage
	},
	{
		path: 'control-panel',
		component: ControlPanelPage,
		canActivate: [AuthGuard]
	},
	{
		path: 'user/:id',
		component: UserInfoPage,
		canActivate: [AuthGuard]
	},
	{
		path: 'user/:id/ratings',
		component: UserRatingsView,
		canActivate: [AuthGuard]
	},
	{
		path: '',
		component: HomePage,
		pathMatch: 'full'
	}];
