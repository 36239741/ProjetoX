import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CovalentDataTableModule } from '@covalent/core/data-table';
import {TableComponent} from './table.component';

@NgModule({
  declarations: [TableComponent],
  imports: [
    CommonModule,
    CovalentDataTableModule
  ],
  exports: [TableComponent]
})
export class TableModule { }
