import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {RideRequestFullResponse} from '../../interfaces/ride-request.interface';
import {Observable} from 'rxjs';
import {RideRequestService} from '../../services/ride-request.service';
import {MatDialog} from '@angular/material/dialog';
import {SubmitRatingDialog} from '../../dialogs/submit-rating/submit-rating.dialog';
import {UINotificationsService} from '../../services/ui-notifications-service';
import {TranslateService} from "@ngx-translate/core";
import {Title} from "@angular/platform-browser";
import {submitRatingAction} from "../../constants/actions.constants";

@Component({
	templateUrl: './control-panel.page.html',
	styleUrls: ['./control-panel.page.scss']
})
export class ControlPanelPage implements OnInit {
	sentRideRequests$: Observable<RideRequestFullResponse[]>;
	receivedRideRequests$: Observable<RideRequestFullResponse[]>;

	constructor(private authService: AuthService,
							private rideRequestService: RideRequestService,
							private _notificationService: UINotificationsService,
							private _dialog: MatDialog,
							private translate: TranslateService,
							private titleService: Title) {
	}

	submit(formValues: any) {
		this.authService.editProfile(formValues).subscribe(user => {
			this.authService.resetUserObservable();
			this.authService.setLoggedUser(user);
			this.changeLang(formValues['defaultLanguage'].toLowerCase())
		});
	}

	changeLang(lang: string) {
		this.translate.use(lang);
		this.translate.setDefaultLang(lang);
		this.translate.get('SITE_TITLE').subscribe(title => this.titleService.setTitle(title));
	}

	ngOnInit(): void {
		this.sentRideRequests$ = this.rideRequestService.getSentRequests();
		this.receivedRideRequests$ = this.rideRequestService.getReceivedRequests();
	}

	takeAction(event: any) {
		if (event.action != submitRatingAction) {
			this.rideRequestService.changeRequestStatus(event.element, event.action).subscribe(() => {
				this.sentRideRequests$ = this.rideRequestService.getSentRequests();
				this._notificationService.success('RIDE_REQUEST_STATUS_CHANGE_SUCCESS', 'ACTION_SUCCESS');
			}, () => {
				this._notificationService.error('RIDE_REQUEST_STATUS_CHANGE_FAIL', 'ACTION_FAIL');
			});
		} else {
			this._dialog.open(SubmitRatingDialog, {
				data: event.id,
				height: '400px',
				width: '600px'
			});
		}
	}
}
