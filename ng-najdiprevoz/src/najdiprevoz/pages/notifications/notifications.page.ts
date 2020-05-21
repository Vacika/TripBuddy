import {Component, OnInit} from "@angular/core";
import {NotificationService} from "../../services/notification.service";
import {NotificationResponse} from "../../interfaces/notification.interface";

@Component({
	templateUrl: './notifications.page.html',
	styleUrls: ['./notifications.page.scss']
})
export class NotificationListPage implements OnInit {
	notifications : NotificationResponse[];
	constructor(private notificationService: NotificationService) {
	}

	ngOnInit() {
		this.notificationService.fetchUserNotifications().subscribe(response=> this.notifications=response);
	}

	takeAction(notification: NotificationResponse, action: string) {
		this.notificationService.takeAction(notification.id, action).subscribe(it=>console.log(it));
	}

	getClassForAction(action: string): string {
		switch(action){
			case "MARK_AS_SEEN": {
				return "mat-primary";
			}
			case "APPROVE":{
				return "success"
			}
			case "RATING_ALLOWED":{
				return "success"
			}
			case "DENY":{
				return "danger"
			}
			case "CANCEL":{
				return "warn"
			}
		}
	}
}
