import {Component, OnInit} from "@angular/core";
import {RatingService} from "../../services/rating.service";
import {RatingCustomResponse} from "../../interfaces/rating.interface";
import {ActivatedRoute} from "@angular/router";
import {TripDetailsDialog} from "../../dialogs/trip-details-dialog/trip-details.dialog";
import {MatDialog} from "@angular/material/dialog";

@Component({
	selector: 'user-ratings',
	templateUrl: './user-ratings.view.html',
	styleUrls: ['./user-ratings.view.scss']
})
export class UserRatingsView implements OnInit {
	ratings: RatingCustomResponse[] = [];

	constructor(private ratingService: RatingService,
							private _route: ActivatedRoute,
							private _dialog: MatDialog) {
	}

	ngOnInit() {
		let userId = this._route.snapshot.params.id;
		this.ratingService.getRatingsForUser(userId).subscribe(response => this.ratings = response);
	}

	rideDetails(tripId: number) {
		const dialogRef = this._dialog.open(TripDetailsDialog, {
			minHeight: '384px',
			width: '500px',
			data: tripId
		});
	}
}
