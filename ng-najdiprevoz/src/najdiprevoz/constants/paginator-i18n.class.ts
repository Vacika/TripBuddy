import {TranslateService} from '@ngx-translate/core';
import {HostBinding, Injectable, OnInit} from '@angular/core';
import {MatPaginatorIntl} from "@angular/material/paginator";

const ITEMS_PER_PAGE = 'ITEMS_PER_PAGE';
const NEXT_PAGE = 'NEXT_PAGE';
const PREV_PAGE = 'PREV_PAGE';
const FIRST_PAGE = 'FIRST_PAGE';
const LAST_PAGE = 'LAST_PAGE';

@Injectable()
export class MatPaginatorI18nService extends MatPaginatorIntl {
	public constructor(private translate: TranslateService) {
		super();
		this.getAndInitTranslations();
		this.translate.onLangChange.subscribe((e: Event) => {
			this.getAndInitTranslations();
		})
	}

	public getRangeLabel = (page: number, pageSize: number, length: number): string => {
		if (length === 0 || pageSize === 0) {
			return `0 / ${length}`;
		}
		length = Math.max(length, 0);
		const startIndex: number = page * pageSize;
		const endIndex: number = startIndex < length
			? Math.min(startIndex + pageSize, length)
			: startIndex + pageSize;

		return `${startIndex + 1} - ${endIndex} / ${length}`;
	};

	public getAndInitTranslations(): void {
		this.translate.get([
			ITEMS_PER_PAGE,
			NEXT_PAGE,
			PREV_PAGE,
			FIRST_PAGE,
			LAST_PAGE,
		])
			.subscribe((translation: any) => {
				this.itemsPerPageLabel = translation[ITEMS_PER_PAGE];
				this.nextPageLabel = translation[NEXT_PAGE];
				this.previousPageLabel = translation[PREV_PAGE];
				this.firstPageLabel = translation[FIRST_PAGE];
				this.lastPageLabel = translation[LAST_PAGE];

				this.changes.next();
			});
	}
}