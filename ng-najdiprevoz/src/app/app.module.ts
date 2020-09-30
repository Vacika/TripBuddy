import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { TripListPage } from '../najdiprevoz/pages/trip-list/trip-list.page';
import { RouterModule } from '@angular/router';
import { appRoutes } from './routing.module';
import { TripService } from '../najdiprevoz/services/trip.service';
import { HTTP_INTERCEPTORS, HttpClient, HttpClientModule } from '@angular/common/http';
import { TripDetailsPage } from '../najdiprevoz/pages/trip-details/trip-details.page';
import { CityService } from '../najdiprevoz/services/city.service';
import { NavMenuComponent } from '../najdiprevoz/components/nav-menu.component';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { MaterialModules } from './material/material.module';
import { LoginPage } from '../najdiprevoz/pages/login/login.page';
import { StarRatingComponent } from '../najdiprevoz/components/star-rating/star-rating.component';
import { MatTooltipModule } from '@angular/material/tooltip';
import { CreateTripPage } from '../najdiprevoz/pages/create-trip/create-trip.page';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { OwlDateTimeModule, OwlNativeDateTimeModule } from 'ng-pick-datetime';
import { TripListView } from '../najdiprevoz/views/trip-list/trip-list.view';
import { SearchTripsPage } from '../najdiprevoz/pages/find-trips/search-trips.page';
import { HelperService } from '../najdiprevoz/services/helper.service';
import { TripDetailsDialog } from '../najdiprevoz/dialogs/trip-details-dialog/trip-details.dialog';
import { MatDialogModule } from '@angular/material/dialog';
import { RideRequestService } from '../najdiprevoz/services/ride-request.service';
import { TripConfirmReservationDialog } from '../najdiprevoz/dialogs/trip-confirm-reservation/trip-confirm-reservation.dialog';
import { MatRadioModule } from '@angular/material/radio';
import { NotificationListPage } from '../najdiprevoz/pages/notifications/notifications.page';
import { NotificationService } from '../najdiprevoz/services/notification.service';
import { RegisterPage } from '../najdiprevoz/pages/register-user/register.page';
import { ErrorInterceptor } from '../najdiprevoz/http.interceptor';
import { ControlPanelPage } from '../najdiprevoz/pages/control-panel/control-panel.page';
import { ProfileSettingsView } from '../najdiprevoz/views/profile-settings/profile-settings.view';
import { MatMenuModule } from '@angular/material/menu';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatTabsModule } from '@angular/material/tabs';
import { MyRatingsView } from '../najdiprevoz/views/my-ratings/my-ratings.view';
import { RatingService } from '../najdiprevoz/services/rating.service';
import { RideRequestsComponent } from '../najdiprevoz/components/ride-requests/ride-requests.component';
import { DataTableComponent } from '../najdiprevoz/components/data-table/data-table.component';
import { MatTableModule } from '@angular/material/table';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatPaginatorModule } from '@angular/material/paginator';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSortModule } from '@angular/material/sort';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatInputModule } from '@angular/material/input';
import { SubmitRatingDialog } from '../najdiprevoz/dialogs/submit-rating/submit-rating.dialog';
import { TripsComponent } from '../najdiprevoz/components/trips/trips.component';
import { ToastrModule } from 'ngx-toastr';
import { UINotificationsService } from '../najdiprevoz/services/ui-notifications-service';
import { LoaderService } from '../najdiprevoz/services/loader.service';
import { LoaderComponent } from '../najdiprevoz/components/loader/loader.component';
import { UserInfoPage } from '../najdiprevoz/pages/user-info/user-info.page';
import { UserRatingsView } from '../najdiprevoz/views/view-ratings-for-user/user-ratings.view';
import { HomePage } from '../najdiprevoz/pages/landing-page/home-page.component';
import { PasswordForgotService } from '../najdiprevoz/services/password-forgot.service';
import { PasswordResetService } from '../najdiprevoz/services/password-reset.service';
import { PasswordForgotPage } from '../najdiprevoz/pages/password-forgot/password-forgot.page';
import { PasswordResetPage } from '../najdiprevoz/pages/password-reset/password-reset.page';
import { ActivateUserPage } from '../najdiprevoz/pages/activate-user/activate-user.page';

const SERVICES = [
	LoaderService,
	TripService,
	CityService, HelperService,
	RideRequestService,
	NotificationService,
	PasswordForgotService,
	PasswordResetService,
	RatingService,
	UINotificationsService];
const DIALOGS = [TripDetailsDialog, TripConfirmReservationDialog, SubmitRatingDialog];
const PAGES = [
	ActivateUserPage,
	PasswordResetPage,
	PasswordForgotPage,
	LoaderComponent,
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
	DataTableComponent,
	TripsComponent,
	UserInfoPage,
	UserRatingsView,
	HomePage];

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
		ToastrModule.forRoot(),
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
		MatProgressSpinnerModule],
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
