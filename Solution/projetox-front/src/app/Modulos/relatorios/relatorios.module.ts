import { MaterialModule } from './../../core/material/material.module';
import { LayoutModule } from './../Layout/layout.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { YearMonthPickerComponent } from './year-month-picker/year-month-picker.component';
import { RelatoriosComponent } from './relatorios.component';
import { RelatorioRoutingModule } from './relatorios-routing.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TableMaterialModule } from '../../shared/components/table-material/table-material.module';



@NgModule({
  declarations: [RelatoriosComponent, YearMonthPickerComponent],
  imports: [
    CommonModule,
    RelatorioRoutingModule,
    LayoutModule,
    MaterialModule,
    FormsModule,
    ReactiveFormsModule,
    TableMaterialModule

  ]
})
export class RelatoriosModule { }
