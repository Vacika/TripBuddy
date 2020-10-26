import {DataTableColumnType} from "./enums/column-type.enum";

export interface DataTableColumn {
	name: string;
	translationLabel: string;
	type: DataTableColumnType;
	actionName?: string;
	dateFormat?: string
	sortable: boolean,
	matIconName?: string
}