import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';

@Injectable({ providedIn: 'root' })
export class UINotificationsService {

	constructor(private _toastr: ToastrService,
							private _translateService: TranslateService) { }

	success(description: string, title: string = ''): void {
		this._translateService.get([description, title])
			.subscribe(translations => this._toastr.success(translations[description], translations[title]));
	}

	error(description: string, title: string = ''): void {
		this._translateService.get([description, title])
			.subscribe(translations => this._toastr.error(translations[description], translations[title]));
	}

	info(description: string, title: string = ''): void {
		this._translateService.get([description, title])
			.subscribe(translations => this._toastr.info(translations[description], translations[title]));
	}

	warning(description: string, title: string = ''): void {
		this._translateService.get([description, title])
			.subscribe(translations => this._toastr.warning(translations[description], translations[title]));
	}

}
