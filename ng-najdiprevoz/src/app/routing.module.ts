import {Routes} from "@angular/router";
import {TripListPage} from "../najdiprevoz/pages/trip-list/trip-list.page";
import {TripPage} from "../najdiprevoz/pages/trip/trip.page";

export const appRoutes: Routes = [
  { path: 'trips/:id', component: TripPage },
  { path: 'trips', component: TripListPage },

  { path: '',
    redirectTo: '/trips',
    pathMatch: 'full'
  }];
