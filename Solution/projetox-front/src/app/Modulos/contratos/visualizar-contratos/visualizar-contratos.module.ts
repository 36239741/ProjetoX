import { FlexLayoutModule } from '@angular/flex-layout';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CovalentDataTableModule } from '@covalent/core/data-table';
import { VisualizarContratosComponent } from './visualizar-contratos.component';
import { HttpClientModule } from '@angular/common/http';
import { FilterModule } from './filter/filter.module';
import { CovalentPagingModule } from '@covalent/core/paging';
import { MatSelectModule } from '@angular/material/select';
import { ImportModuleButton } from './import/importButton/import-button.module';
import { ImportModule } from './import/import.module';


@NgModule({
  declarations: [VisualizarContratosComponent],
  imports: [
    CommonModule,
    CovalentDataTableModule,
    HttpClientModule,
    FilterModule,
    FlexLayoutModule,
    CovalentPagingModule,
    MatSelectModule,
    ImportModuleButton,
    ImportModule,
  ],
  exports: [
    VisualizarContratosComponent
  ]
})
export class VisualizarContratosModule { }
