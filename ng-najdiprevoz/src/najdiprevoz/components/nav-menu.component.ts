import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from "@angular/core";
import {FormControl} from "@angular/forms";

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

	ngOnInit(): void {
		this.selectedLangControl.valueChanges.subscribe(newLang => this.switchLangEmitter.emit(newLang))
	}

	onFlagClick() {
		this.languageSelect.toggle();
	}
}
