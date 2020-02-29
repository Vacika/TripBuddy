import {Component, OnInit} from "@angular/core";
import {TripService} from "../../services/trip.service";
import {TripResponse} from "../../interfaces/trip-response.interface";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  templateUrl: './trip-list.page.html',
  styleUrls: ['./trip-list.page.scss']
})
export class TripListPage implements OnInit {
  allTrips: TripResponse[] = [];

  constructor(private _service: TripService,
              private _router: Router) {}

  ngOnInit(): void {
    this._service.getAllActiveTripsWithFreeSeats().subscribe(response => this.allTrips = response) // fetch all active rides
  }
}
