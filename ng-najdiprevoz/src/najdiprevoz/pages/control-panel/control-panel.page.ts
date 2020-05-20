import {Component, OnInit} from "@angular/core";
import {AuthService} from "../../services/auth.service";
import {RideRequestFullResponse, RideRequestResponse} from "../../interfaces/ride-request.interface";
import {Observable} from "rxjs";
import {RideRequestService} from "../../services/ride-request.service";

@Component({
	templateUrl: './control-panel.page.html',
	styleUrls: ['./control-panel.page.scss']
})
export class ControlPanelPage implements OnInit {
	sentRideRequests$: Observable<RideRequestFullResponse[]>;
	receivedRideRequests$: Observable<RideRequestFullResponse[]>;

	constructor(private authService: AuthService,
				private rideRequestService: RideRequestService) {
	}

	submit(formValues: any) {
		this.authService.editProfile(formValues).subscribe(user => {
			this.authService.resetUserObservable();
			this.authService.setLoggedUser(user)
		})
	}

	ngOnInit(): void {
		this.sentRideRequests$ = this.rideRequestService.getSentRequests();
		this.receivedRideRequests$ = this.rideRequestService.getReceivedRequests();
	}

	takeAction(event: any) {
		this.rideRequestService.changeRequestStatus(event.id, event.action).subscribe(it=>console.log(it))
	}
}
