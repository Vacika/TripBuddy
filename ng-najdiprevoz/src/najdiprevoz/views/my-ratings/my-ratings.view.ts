import {Component, OnInit} from "@angular/core";
import {RatingService} from "../../services/rating.service";
import {RatingCustomResponse} from "../../interfaces/rating.interface";

@Component({
	selector: 'my-ratings',
	templateUrl: './my-ratings.view.html',
	styleUrls: ['./my-ratings.view.scss']
})
export class MyRatingsView implements OnInit {
	ratings: RatingCustomResponse[] = [];

	constructor(private ratingService: RatingService) {

	}

	ngOnInit() {
		this.ratingService.getMyRatings().subscribe(response => this.ratings = response);
	}
}