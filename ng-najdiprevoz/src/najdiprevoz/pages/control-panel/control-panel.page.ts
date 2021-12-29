import {Component, OnInit} from '@angular/core';
import {ReservationRequestFullResponse} from '../../interfaces/ride-request.interface';
import {Observable} from 'rxjs';
import {ReservationRequestService} from '../../services/reservation-request.service';
import {MatDialog} from '@angular/material/dialog';
import {SubmitRatingDialog} from '../../dialogs/submit-rating/submit-rating.dialog';
import {UINotificationsService} from '../../services/util/ui-notifications-service';
import {TranslateService} from "@ngx-translate/core";
import {Title} from "@angular/platform-browser";
import {SEE_TRIP_REQUEST_INFO_ACTION, SUBMIT_RATING_ACTION} from "../../constants/actions.constants";
import {ReservationDetailsDialog} from "../../dialogs/reservation-details/reservation-details.dialog";
import {UserService} from "../../services/user.service";

@Component({
	templateUrl: './control-panel.page.html',
	styleUrls: ['./control-panel.page.scss']
})
export class ControlPanelPage implements OnInit {
	sentReservationRequests$: Observable<ReservationRequestFullResponse[]>;
	receivedReservationRequests$: Observable<ReservationRequestFullResponse[]>;

	constructor(private _userService: UserService,
							private _reservationRequestService: ReservationRequestService,
							private _notificationService: UINotificationsService,
							private _dialog: MatDialog,
							private _translate: TranslateService,
							private _titleService: Title) {
	}

	submit(formValues: any) {
		this._userService.editProfile(formValues).subscribe(user => {
			this._userService.resetUserObservable();
			this._userService.setLoggedUser(user);
			this.changeLang(formValues['defaultLanguage'].toLowerCase())
		});
	}

	changeLang(lang: string) {
		this._translate.use(lang);
		this._translate.setDefaultLang(lang);
		this._translate.get('SITE_TITLE').subscribe(title => this._titleService.setTitle(title));
	}

	ngOnInit(): void {
		this.sentReservationRequests$ = this._reservationRequestService.getSentRequests();
		this.receivedReservationRequests$ = this._reservationRequestService.getReceivedRequests();
	}

	takeAction(event: any) {
		if (event.action != SUBMIT_RATING_ACTION && event.action !== SEE_TRIP_REQUEST_INFO_ACTION) {
			this._reservationRequestService.changeRequestStatus(event.element, event.action).subscribe(() => {
				this.sentReservationRequests$ = this._reservationRequestService.getSentRequests();
				this._notificationService.success('RIDE_REQUEST_STATUS_CHANGE_SUCCESS');
			}, () => {
				this._notificationService.error('RIDE_REQUEST_STATUS_CHANGE_FAIL');
			});
		} else if (event.action == SUBMIT_RATING_ACTION) {
			this._dialog.open(SubmitRatingDialog, {
				data: event.id,
				height: '400px',
				width: '600px'
			});
		} else if (event.action === SEE_TRIP_REQUEST_INFO_ACTION) {
			this._dialog.open(ReservationDetailsDialog, {
				data: event.element as ReservationRequestFullResponse,
				height: '305px',
				width: '600px'
			})
		}
	}
}
