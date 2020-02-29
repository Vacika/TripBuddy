import {Component, OnInit} from "@angular/core";
import {TripService} from "../../services/trip.service";
import {ActivatedRoute} from "@angular/router";
import {Trip} from "../../interfaces/trip.interface";

@Component({
  templateUrl: './trip.page.html',
  styleUrls: ['./trip.page.scss']
})
export class TripPage implements OnInit {
  trip: Trip;
  tripId: number;

  constructor(private _service: TripService,
              private _route: ActivatedRoute) {
    this.tripId = this._route.snapshot.params.id;

  };

  ngOnInit(): void {
    if (this.tripId) {
      console.log('tripId:', this.tripId);
      this._service.getTripInformation(this.tripId).subscribe(response => this.trip = response);
      console.log(this.trip);
    }
  }

}
