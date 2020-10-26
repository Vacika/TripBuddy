import {DataTableColumnType} from "./enums/column-type.enum";

export interface DataTableColumn {
	name: string;
	translationLabel: string;
	type: DataTableColumnType;
	subType?: string;
	dateFormat?: string
	sortable: boolean
}