import {CommonModule} from '@angular/common';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {MatCardModule} from '@angular/material/card';
import {MatSelectModule} from '@angular/material/select';
import {ReactiveFormsModule} from '@angular/forms';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {MatDividerModule} from '@angular/material/divider';
import {MatTableModule} from "@angular/material/table";
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatSortModule} from "@angular/material/sort";
import {NgModule} from "@angular/core";
import {MatDialogModule} from "@angular/material/dialog";
import {MatTooltipModule} from "@angular/material/tooltip";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatRadioModule} from "@angular/material/radio";
import {MatMenuModule} from "@angular/material/menu";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatNativeDateModule} from "@angular/material/core";
import {MatTabsModule} from "@angular/material/tabs";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";

const APP_MATERIAL_MODULES = [
	CommonModule,
	MatToolbarModule,
	MatIconModule,
	MatButtonModule,
	MatCardModule,
	MatSelectModule,
	ReactiveFormsModule,
	MatFormFieldModule,
	MatInputModule,
	MatSnackBarModule,
	MatDividerModule,
	MatTableModule,
	MatSortModule,
	MatDialogModule,
	MatTooltipModule,
	MatCheckboxModule,
	MatRadioModule,
	MatMenuModule,
	MatDatepickerModule,
	MatNativeDateModule,
	MatTabsModule,
	MatPaginatorModule,
	MatProgressSpinnerModule,
];
@NgModule({
	declarations:[],
	imports: [...APP_MATERIAL_MODULES],
	exports:[...APP_MATERIAL_MODULES]
})
export class AppMaterialModule {
}