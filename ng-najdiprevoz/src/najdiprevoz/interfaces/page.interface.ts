import {Sort} from "@angular/material/sort";

export interface Pageable {
	pageNumber: number;
	pageSize: number;
	sort?: Sort
}

export interface Page<T> {
	totalElements: number;
	totalPages: number;
	content: T;
	number: number;
	numberOfElements: number;
	size: number;
	sort?: Sort;
	pageable: Pageable
}