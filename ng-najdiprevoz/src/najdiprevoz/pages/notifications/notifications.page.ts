import {Component, OnInit} from '@angular/core';
import {NotificationService} from '../../services/notification.service';
import {NotificationResponse} from '../../interfaces/notification.interface';
import {MatDialog} from '@angular/material/dialog';
import {SubmitRatingDialog} from '../../dialogs/submit-rating/submit-rating.dialog';
import {UINotificationsService} from '../../services/util/ui-notifications-service';
import {
	APPROVE_ACTION,
	CANCEL_ACTION,
	DENY_ACTION,
	MARK_AS_SEEN_ACTION,
	RATING_ALLOWED_ACTION,
	SUBMIT_RATING_ACTION
} from "../../constants/actions.constants";

@Component({
	templateUrl: './notifications.page.html',
	styleUrls: ['./notifications.page.scss']
})
export class NotificationListPage implements OnInit {
	notifications: NotificationResponse[];

	constructor(private _notificationService: NotificationService,
							private _uiNotificationsService: UINotificationsService,
							private _dialog: MatDialog) {
	}

	ngOnInit() {
		// Fetch notifications every 15 seconds
		setInterval(() =>
			this._notificationService.fetchUserNotifications()
				.subscribe(response => this.notifications = response), 15000);
	}

	takeAction(notification: NotificationResponse, action: string) {
		if (action != SUBMIT_RATING_ACTION) {
			this._notificationService.takeAction(notification.id, action).subscribe(response => {
				this._uiNotificationsService.successAction();
				this.notifications = response;
			}, () => {
				this._uiNotificationsService.errorAction();
			});
		} else {
			this._dialog.open(SubmitRatingDialog, {
				height: '400px',
				width: '600px',
				data: notification.reservationRequestId
			});
		}
	}

	getClassForAction(action: string): string {
		switch (action) {
			case MARK_AS_SEEN_ACTION: {
				return 'mat-primary';
			}
			case APPROVE_ACTION: {
				return 'success';
			}
			case RATING_ALLOWED_ACTION: {
				return 'success';
			}
			case DENY_ACTION: {
				return 'danger';
			}
			case CANCEL_ACTION: {
				return 'warn';
			}
		}
	}
}
