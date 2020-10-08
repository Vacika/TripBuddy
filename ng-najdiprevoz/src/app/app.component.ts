import {Component} from '@angular/core';
import {TranslateService} from "@ngx-translate/core";
import {Title} from "@angular/platform-browser";
import {isNotNullOrUndefined} from "codelyzer/util/isNotNullOrUndefined";

@Component({
	selector: 'app-root',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.css']
})
export class AppComponent {
	constructor(public translate: TranslateService,
							private titleService: Title) {
		translate.addLangs(['mk', 'al', 'en']);
		translate.setDefaultLang('mk');
		if (isNotNullOrUndefined(localStorage.getItem('lang'))) {
			this.changeLang(localStorage.getItem('lang'));
		} else {
			this.changeLang('mk');
		}
	}

	changeLang(lang: string) {
		this.translate.use(lang);
		this.translate.setDefaultLang(lang);
		this.translate.get('SITE_TITLE').subscribe(title => this.titleService.setTitle(title));
	}
}
