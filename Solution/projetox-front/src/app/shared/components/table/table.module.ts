import { MatIconModule } from '@angular/material/icon';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CovalentDataTableModule } from '@covalent/core/data-table';
import {TableComponent} from './table.component';
import { MatTooltipModule } from '@angular/material';

@NgModule({
  declarations: [TableComponent],
  imports: [
    CommonModule,
    CovalentDataTableModule,
    MatIconModule,
    MatTooltipModule
  ],
  exports: [TableComponent]
})
export class TableModule { }
