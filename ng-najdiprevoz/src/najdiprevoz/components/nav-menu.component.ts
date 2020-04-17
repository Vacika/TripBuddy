import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from "@angular/core";
import {FormControl} from "@angular/forms";
import {AuthService} from "../services/auth.service";

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

	constructor(private loginService: AuthService) {
	}

	ngOnInit(): void {
		this.selectedLangControl.valueChanges
			.subscribe(newLang => this.switchLangEmitter.emit(newLang))
	}

	onFlagClick() {
		this.languageSelect.toggle();
	}

	private get authenticated(): boolean {
		console.log( !!this.loginService.getLoggedUser());
		return !!this.loginService.getLoggedUser()
	}

	logout() {
		this.loginService.logout();
	}
}
