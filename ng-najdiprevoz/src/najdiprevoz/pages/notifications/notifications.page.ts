import { Component, OnInit } from '@angular/core';
import { NotificationService } from '../../services/notification.service';
import { NotificationResponse } from '../../interfaces/notification.interface';
import { MatDialog } from '@angular/material/dialog';
import { TripConfirmReservationDialog } from '../../dialogs/trip-confirm-reservation/trip-confirm-reservation.dialog';
import { SubmitRatingDialog } from '../../dialogs/submit-rating/submit-rating.dialog';

@Component({
	templateUrl: './notifications.page.html',
	styleUrls: ['./notifications.page.scss']
})
export class NotificationListPage implements OnInit {
	notifications: NotificationResponse[];

	constructor(private _notificationService: NotificationService,
							private _dialog: MatDialog) {
	}

	ngOnInit() {
		this._notificationService.fetchUserNotifications().subscribe(response => this.notifications = response);
	}

	takeAction(notification: NotificationResponse, action: string) {
		if (action != 'SUBMIT_RATING') {
			this._notificationService.takeAction(notification.id, action).subscribe(it => console.log(it));
		} else {
			this._dialog.open(SubmitRatingDialog, {
				height: '400px',
				width: '600px',
				data: notification.rideRequestId
			});
		}
	}

	getClassForAction(action: string): string {
		switch (action) {
			case 'MARK_AS_SEEN': {
				return 'mat-primary';
			}
			case 'APPROVE': {
				return 'success';
			}
			case 'RATING_ALLOWED': {
				return 'success';
			}
			case 'DENY': {
				return 'danger';
			}
			case 'CANCEL': {
				return 'warn';
			}
		}
	}
}
