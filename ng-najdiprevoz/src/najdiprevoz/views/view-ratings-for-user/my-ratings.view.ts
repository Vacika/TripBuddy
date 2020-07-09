import {Component, OnInit} from "@angular/core";
import {RatingService} from "../../services/rating.service";
import {RatingCustomResponse} from "../../interfaces/rating.interface";
import {ActivatedRoute} from "@angular/router";

@Component({
	selector: 'my-ratings',
	templateUrl: './my-ratings.view.html',
	styleUrls: ['./my-ratings.view.scss']
})
export class UserRatingsView implements OnInit {
	ratings: RatingCustomResponse[] = [];

	constructor(private ratingService: RatingService,
							private _route: ActivatedRoute) {
	}

	ngOnInit() {
		let userId = this._route.snapshot.params.id;
		console.log("USERID:", userId);
		this.ratingService.getRatingsForUser(userId).subscribe(response => this.ratings = response);
	}
}
