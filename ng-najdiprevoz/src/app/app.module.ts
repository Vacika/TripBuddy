import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {RideListPage} from "../najdiprevoz/pages/ride-list/ride-list.page";

@NgModule({
  declarations: [
    AppComponent,
    RideListPage
  ],
  imports: [
    BrowserModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
