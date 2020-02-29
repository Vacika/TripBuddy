import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppComponent} from './app.component';
import {TripListPage} from "../najdiprevoz/pages/trip-list/trip-list.page";
import {RouterModule} from "@angular/router";
import {appRoutes} from "./routing.module";
import {TripService} from "../najdiprevoz/services/trip.service";
import {HttpClientModule} from "@angular/common/http";
import {TripPage} from "../najdiprevoz/pages/trip/trip.page";
import {CityService} from "../najdiprevoz/services/city.service";


const services = [TripService,CityService];

@NgModule({
  declarations: [
    AppComponent,
    TripListPage,
    TripPage
  ],
  imports: [
    RouterModule.forRoot(appRoutes,
      {enableTracing: true}),
    BrowserModule,
    HttpClientModule
  ],
  providers: [...services],
  bootstrap: [AppComponent]
})
export class AppModule {
}
