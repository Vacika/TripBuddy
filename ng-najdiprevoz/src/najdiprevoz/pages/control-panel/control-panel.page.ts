import {Component, OnInit} from "@angular/core";
import {AuthService} from "../../services/auth.service";

@Component({
	templateUrl: './control-panel.page.html',
	styleUrls: ['./control-panel.page.scss']
})
export class ControlPanelPage {

	constructor(private authService: AuthService){}
	submit(formValues: any) {
		this.authService.editProfile(formValues).subscribe(it=>console.log(it))
	}
}
