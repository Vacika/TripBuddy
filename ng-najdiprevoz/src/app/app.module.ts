import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { TripListPage } from '../najdiprevoz/pages/trip-list/trip-list.page';
import { RouterModule } from '@angular/router';
import { appRoutes } from './routing.module';
import { TripService } from '../najdiprevoz/services/trip.service';
import { HttpClient, HttpClientModule } from '@angular/common/http';
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

const services = [TripService, CityService];

@NgModule({
	declarations: [
		AppComponent,
		TripListPage,
		TripDetailsPage,
		NavMenuComponent,
		LoginPage,
		StarRatingComponent,
		CreateTripPage
	],
	imports: [
		RouterModule.forRoot(appRoutes),
		BrowserModule,
		OwlDateTimeModule,
		OwlNativeDateTimeModule,
		HttpClientModule,
		MaterialModules,
		TranslateModule.forRoot({
			loader: {
				provide: TranslateLoader,
				useFactory: (httpTranslateLoader),
				deps: [HttpClient]
			}
		}),
		MatTooltipModule,
		MatCheckboxModule
	],
	providers: [...services],
	bootstrap: [AppComponent]
})
export class AppModule {
}

export function httpTranslateLoader(http: HttpClient) {
	return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}
