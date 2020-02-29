import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppComponent} from './app.component';
import {TripListPage} from "../najdiprevoz/pages/trip-list/trip-list.page";
import {RouterModule} from "@angular/router";
import {appRoutes} from "./routing.module";
import {TripService} from "../najdiprevoz/services/trip.service";
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {TripDetailsPage} from "../najdiprevoz/pages/trip-details/trip-details.page";
import {CityService} from "../najdiprevoz/services/city.service";
import {NavMenuComponent} from "../najdiprevoz/components/nav-menu.component";
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';


const services = [TripService, CityService];

@NgModule({
  declarations: [
    AppComponent,
    TripListPage,
    TripDetailsPage,
    NavMenuComponent
  ],
  imports: [
    RouterModule.forRoot(appRoutes),
    BrowserModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatIconModule,
    MatButtonModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: (httpTranslateLoader),
        deps: [HttpClient]
      }
    })
  ],
  providers: [...services],
  bootstrap: [AppComponent]
})
export class AppModule {
}


export function httpTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/','.json');
}
