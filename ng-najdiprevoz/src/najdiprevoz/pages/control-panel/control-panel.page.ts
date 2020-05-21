import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { RideRequestFullResponse } from '../../interfaces/ride-request.interface';
import { Observable } from 'rxjs';
import { RideRequestService } from '../../services/ride-request.service';
import { MatDialog } from '@angular/material/dialog';
import { SubmitRatingDialog } from '../../dialogs/submit-rating/submit-rating.dialog';
import { UINotificationsService } from '../../services/ui-notifications-service';

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
				private _dialog: MatDialog) {
	}

	submit(formValues: any) {
		this.authService.editProfile(formValues).subscribe(user => {
			this.authService.resetUserObservable();
			this.authService.setLoggedUser(user);
		});
	}

	ngOnInit(): void {
		this.sentRideRequests$ = this.rideRequestService.getSentRequests();
		this.receivedRideRequests$ = this.rideRequestService.getReceivedRequests();
	}

	takeAction(event: any) {
		if (event.action != 'SUBMIT_RATING') {
			this.rideRequestService.changeRequestStatus(event.id, event.action).subscribe(()=>{
				this._notificationService.success("RIDE_REQUEST_STATUS_CHANGE_SUCCESS","ACTION_SUCCESS")
			},()=>{
				this._notificationService.success("RIDE_REQUEST_STATUS_CHANGE_FAIL","ACTION_FAIL")
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
