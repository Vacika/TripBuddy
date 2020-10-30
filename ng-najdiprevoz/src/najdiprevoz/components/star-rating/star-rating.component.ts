import {Component, Input, OnInit, ViewEncapsulation} from '@angular/core';
import {MatSnackBar} from "@angular/material/snack-bar";
import {TranslateService} from '@ngx-translate/core';

@Component({
	selector: 'mat-star-rating',
	templateUrl: './star-rating.component.html',
	styleUrls: ['./star-rating.component.scss'],
	encapsulation: ViewEncapsulation.Emulated
})
export class StarRatingComponent implements OnInit {

	@Input('rating') rating: number = 3;
	@Input('starCount') starCount: number = 5;
	@Input('color') color: string = 'accent';
	snackBarDuration: number = 2000;
	ratingArr = [];

	constructor(private snackBar: MatSnackBar,
							private translateService: TranslateService) {
	}


	ngOnInit() {
		for (let index = 0; index < this.starCount; index++) {
			this.ratingArr.push(index);
		}
	}

	onClick() {
		this.snackBar.open(this.translateService.instant('RATING') + ': ' + this.rating + ' / ' + this.starCount, '', {
			duration: this.snackBarDuration
		});
		return false;
	}

	showIcon(index: number) {
		if (Math.ceil(this.rating) >= index + 1) {
			if (this.rating < index + 1)
				return 'star_half';
			return 'star';
		} else {
			return 'star_border';
		}
	}

}

export enum StarRatingColor {
	primary = "primary",
	accent = "accent",
	warn = "warn"
}
