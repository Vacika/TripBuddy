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

	ngOnInit() {
		this.fetchUsers();
	}

	fetchUsers() {
		this.users$ = this.service.fetchAllUsers();
	}

	onActionTaken(actionData: { element: any; action: string; }) {
		switch (actionData.action) {
			case ACTION_CONSTANTS.banUserAction: {
				this.service.banUser(actionData.element)
					.subscribe(() => {
						this.fetchUsers();
						this._notificationService.success('ACTION_SUCCESS');
						},
						_ => this._notificationService.error('ACTION_FAIL'));
				break;
			}
			case ACTION_CONSTANTS.unBanUserAction: {
				this.service.unBanUser(actionData.element).subscribe(() => {
						this.fetchUsers();
						this._notificationService.success('ACTION_SUCCESS');
					},
					_ => this._notificationService.error('ACTION_FAIL'));
				break;
			}
			case ACTION_CONSTANTS.activateUserAction: {
				this.service.unBanUser(actionData.element).subscribe(() => {
						this.fetchUsers();
						this._notificationService.success('ACTION_SUCCESS');
					},
					_ => this._notificationService.error('ACTION_FAIL'));
				break;
			}
			case ACTION_CONSTANTS.changeUserRoleAction: {
				break;
			}
		}
	}

	get adminTableColumns() {
		return adminTableColumns;
	};
}
