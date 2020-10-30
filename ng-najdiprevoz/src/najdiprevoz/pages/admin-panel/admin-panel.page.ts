import {Component, HostBinding, OnInit} from '@angular/core';
import {UINotificationsService} from '../../services/ui-notifications-service';
import {ActivatedRoute, Router} from '@angular/router';
import {User} from "../../interfaces/user.interface";
import {Observable} from "rxjs";
import {AdminService} from "../../services/admin.service";
import * as ACTION_CONSTANTS from "../../constants/actions.constants"
import {adminTableColumns} from "../../constants/columns.constants";

@Component({
	templateUrl: './admin-panel.page.html',
	styleUrls: ['./admin-panel.page.scss']
})
export class AdminPanelPage implements OnInit {
	@HostBinding('class') classes = 'page';


	users$: Observable<User[]>;

	constructor(private route: ActivatedRoute,
							private router: Router,
							private service: AdminService,
							private _notificationService: UINotificationsService) {
	}

	public get adminTableColumns() {
		return adminTableColumns;
	};

	ngOnInit() {
		this.fetchUsers();
	}

	fetchUsers() {
		this.users$ = this.service.fetchAllUsers();
	}

	onActionTaken(actionData: { element: any; action: string; }) {
		switch (actionData.action) {
			case ACTION_CONSTANTS.BAN_USER_ACTION: {
				this.service.banUser(actionData.element)
					.subscribe(() => {
							this.fetchUsers();
							this._notificationService.successAction();
						},
						_ => this._notificationService.errorAction());
				break;
			}
			case ACTION_CONSTANTS.UNBAN_USER_ACTION: {
				this.service.unBanUser(actionData.element).subscribe(() => {
						this.fetchUsers();
						this._notificationService.successAction();
					},
					_ => this._notificationService.errorAction());
				break;
			}
			case ACTION_CONSTANTS.ACTIVATE_USER_ACTION: {
				this.service.unBanUser(actionData.element).subscribe(() => {
						this.fetchUsers();
						this._notificationService.successAction();
					},
					_ => this._notificationService.errorAction());
				break;
			}
			case ACTION_CONSTANTS.CHANGE_USER_ROLE_ACTION: {
				break;
			}
		}
	}
}
