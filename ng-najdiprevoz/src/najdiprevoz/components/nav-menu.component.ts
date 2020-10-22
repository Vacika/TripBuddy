import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from "@angular/core";
import {FormControl} from "@angular/forms";
import {AuthService} from "../services/auth.service";
import { Router } from '@angular/router';
import { HomePage } from '../pages/landing-page/home-page.component';
import {ADMIN_ROLE} from "../constants/roles.constants";

@Component({
	selector: 'nav-menu',
	templateUrl: './nav-menu.component.html',
	styleUrls: ['./nav-menu.component.scss']
})
export class NavMenuComponent implements OnInit {
	selectedLangControl = new FormControl('');
	@Input() languages: string[];

	@Input() set currentLanguage(lang: string) {
		this.selectedLangControl.setValue(lang)
	};

	@Output() switchLangEmitter = new EventEmitter<string>();
	@ViewChild('languageSelect') languageSelect;

	constructor(private loginService: AuthService,
							private _router: Router) {
	}

	ngOnInit(): void {
		this.selectedLangControl.valueChanges
			.subscribe(newLang => this.switchLangEmitter.emit(newLang))
	}

	onFlagClick() {
		this.languageSelect.toggle();
	}

	get authenticated(): boolean {
		return !!this.loginService.getLoggedUser()
	}

	get isAdmin(): boolean {
		return this.loginService.getLoggedUser()?.authorities[0].authority === ADMIN_ROLE;
	}

	logout() {
		this._router.navigate(['/'], {}).then();
		this.loginService.logout();
	}
}
