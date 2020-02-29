import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppComponent} from './app.component';
import {TripListPage} from "../najdiprevoz/pages/trip-list/trip-list.page";
import {RouterModule} from "@angular/router";
import {appRoutes} from "./routing.module";
import {TripService} from "../najdiprevoz/services/trip.service";
import {HttpClientModule} from "@angular/common/http";
import {TripDetailsPage} from "../najdiprevoz/pages/trip-details/trip-details.page";
import {CityService} from "../najdiprevoz/services/city.service";
import {NavMenuComponent} from "../najdiprevoz/components/nav-menu.component";
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";


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
    MatButtonModule
  ],
  providers: [...services],
  bootstrap: [AppComponent]
})
export class AppModule {
}
