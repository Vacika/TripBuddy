import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from "@angular/core";
import {FormControl} from "@angular/forms";
import {AuthService} from "../services/auth.service";
import {Router} from '@angular/router';
import {ADMIN_ROLE} from "../constants/roles.constants";

@Component({
	selector: 'nav-menu',
	templateUrl: './nav-menu.component.html',
	styleUrls: ['./nav-menu.component.scss']
})
export class NavMenuComponent implements OnInit {
	selectedLangControl = new FormControl('');
	@Input() languages: string[];
	@Output() switchLangEmitter = new EventEmitter<string>();
	@ViewChild('languageSelect') languageSelect;

	constructor(private _authService: AuthService,
							private _router: Router) {
	}

	@Input() set currentLanguage(lang: string) {
		this.selectedLangControl.setValue(lang)
	};

	get username(): string {
		return this._authService.getLoggedUser().username;
	}
	get authenticated(): boolean {
		return !!this._authService.getLoggedUser()
	}

	get isAdmin(): boolean {
		return this._authService.getLoggedUser()?.authorities[0].authority === ADMIN_ROLE;
	}

	ngOnInit(): void {
		this.selectedLangControl.valueChanges
			.subscribe(newLang => this.switchLangEmitter.emit(newLang))
	}

	onFlagClick() {
		this.languageSelect.toggle();
	}

	logout() {
		this._router.navigate(['/'], {}).then();
		this._authService.logout().subscribe();
	}
}
