import { Component, OnInit } from '@angular/core';
import { UINotificationsService } from '../../services/ui-notifications-service';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
	templateUrl: './activate-user.page.html',
	styleUrls: ['./activate-user.page.scss']
})
export class ActivateUserPage implements OnInit {
	token: string;
	isValid = false;

	constructor(private route: ActivatedRoute,
							private router: Router,
							private userService: AuthService,
							private notificationService: UINotificationsService) {
	}

	ngOnInit() {
		this.route.queryParamMap.subscribe(params => {
			this.token = params.get('token');
			if (this.token) {
				this.userService.activateUser(this.token).subscribe(activationSuccess => {
						this.isValid = activationSuccess;
						if (activationSuccess) {
							this.notificationService.success('USER_ACTIVATE_SUCCESS');
							// this.router.navigate(['']);
						} else {
							this.notificationService.error('USER_ACTIVATE_FAILED');
						}
					},
					_ => this.notificationService.error('USER_ACTIVATE_FAILED'));
			}
		});

	}
}
