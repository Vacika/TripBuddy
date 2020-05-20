import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppComponent} from './app.component';
import {TripListPage} from '../najdiprevoz/pages/trip-list/trip-list.page';
import {RouterModule} from '@angular/router';
import {appRoutes} from './routing.module';
import {TripService} from '../najdiprevoz/services/trip.service';
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from '@angular/common/http';
import {TripDetailsPage} from '../najdiprevoz/pages/trip-details/trip-details.page';
import {CityService} from '../najdiprevoz/services/city.service';
import {NavMenuComponent} from '../najdiprevoz/components/nav-menu.component';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {MaterialModules} from './material/material.module';
import {LoginPage} from '../najdiprevoz/pages/login/login.page';
import {StarRatingComponent} from '../najdiprevoz/components/star-rating/star-rating.component';
import {MatTooltipModule} from '@angular/material/tooltip';
import {CreateTripPage} from '../najdiprevoz/pages/create-trip/create-trip.page';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {OwlDateTimeModule, OwlNativeDateTimeModule} from 'ng-pick-datetime';
import {TripListView} from '../najdiprevoz/views/trip-list/trip-list.view';
import {SearchTripsPage} from '../najdiprevoz/pages/find-trips/search-trips.page';
import {HelperService} from '../najdiprevoz/services/helper.service';
import {TripDetailsDialog} from '../najdiprevoz/dialogs/trip-details-dialog/trip-details.dialog';
import {MatDialogModule} from '@angular/material/dialog';
import {RideRequestService} from '../najdiprevoz/services/ride-request.service';
import {TripConfirmReservationDialog} from "../najdiprevoz/dialogs/trip-confirm-reservation/trip-confirm-reservation.dialog";
import {MatRadioModule} from "@angular/material/radio";
import {NotificationListPage} from "../najdiprevoz/pages/notifications/notifications.page";
import {NotificationService} from "../najdiprevoz/services/notification.service";
import {RegisterPage} from "../najdiprevoz/pages/register-user/register.page";
import {ErrorInterceptor} from "../najdiprevoz/http.interceptor";
import {ControlPanelPage} from "../najdiprevoz/pages/control-panel/control-panel.page";
import {ProfileSettingsView} from "../najdiprevoz/views/profile-settings/profile-settings.view";
import {MatMenuModule} from "@angular/material/menu";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatNativeDateModule} from "@angular/material/core";
import {MatTabsModule} from "@angular/material/tabs";
import {MyRatingsView} from "../najdiprevoz/views/my-ratings/my-ratings.view";
import {RatingService} from "../najdiprevoz/services/rating.service";
import {RideRequestsComponent} from "../najdiprevoz/components/ride-requests/ride-requests.component";
import {RideRequestsTableComponent} from "../najdiprevoz/components/ride-request-table/ride-requests-table.component";
import {MatTableModule} from "@angular/material/table";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MatPaginatorModule} from "@angular/material/paginator";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatSortModule} from "@angular/material/sort";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {MatInputModule} from "@angular/material/input";

const SERVICES = [TripService, CityService, HelperService, RideRequestService, NotificationService, RatingService];
const DIALOGS = [TripDetailsDialog, TripConfirmReservationDialog];
const PAGES = [
	NotificationListPage,
	TripListPage,
	TripDetailsPage,
	NavMenuComponent,
	LoginPage,
	StarRatingComponent,
	CreateTripPage,
	TripListView,
	SearchTripsPage,
	RegisterPage,
	ControlPanelPage,
	ProfileSettingsView,
	MyRatingsView,
	RideRequestsComponent,
	RideRequestsTableComponent];

@NgModule({
	declarations: [
		AppComponent,
		PAGES,
		DIALOGS
	],
	imports: [
		RouterModule.forRoot(appRoutes),
		BrowserModule,
		OwlDateTimeModule,
		OwlNativeDateTimeModule,
		HttpClientModule,
		MaterialModules,
		MatDialogModule,
		BrowserAnimationsModule,
		MatPaginatorModule,
		ReactiveFormsModule,
		TranslateModule.forRoot({
			loader: {
				provide: TranslateLoader,
				useFactory: (httpTranslateLoader),
				deps: [HttpClient]
			}
		}),
		MatTooltipModule,
		MatCheckboxModule,
		MatRadioModule,
		MatMenuModule,
		MatDatepickerModule,
		MatNativeDateModule,
		MatTabsModule,
		MatInputModule,
		MatTableModule,
		MatPaginatorModule,
		MatSortModule,
		MatProgressSpinnerModule
	],
	providers: [...SERVICES, {
		provide: HTTP_INTERCEPTORS,
		useClass: ErrorInterceptor,
		multi: true
	}],
	bootstrap: [AppComponent]
})
export class AppModule {
}

export function httpTranslateLoader(http: HttpClient) {
	return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}
