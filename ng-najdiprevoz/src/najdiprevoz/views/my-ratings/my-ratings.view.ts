import {Component, OnInit} from "@angular/core";
import {RatingService} from "../../services/rating.service";
import {RatingCustomResponse} from "../../interfaces/rating.interface";
import {MatDialog} from '@angular/material/dialog';
import {TripDetailsDialog} from '../../dialogs/trip-details-dialog/trip-details.dialog';

@Component({
	selector: 'my-ratings',
	templateUrl: './my-ratings.view.html',
	styleUrls: ['./my-ratings.view.scss']
})
export class MyRatingsView implements OnInit {
	ratings: RatingCustomResponse[] = [];

	constructor(private ratingService: RatingService,
							private _dialog: MatDialog) {
	}

	ngOnInit() {
		this.ratingService.getMyRatings().subscribe(response => this.ratings = response);
	}

	rideDetails(tripId: number) {
		const dialogRef = this._dialog.open(TripDetailsDialog, {
			minHeight: '384px',
			width: '500px',
			data: tripId
		});
	}
}