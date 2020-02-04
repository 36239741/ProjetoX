import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableMaterialComponent } from './table-material.component';
import {MatTableModule} from '@angular/material/table';

@NgModule({

  declarations: [TableMaterialComponent],
  imports: [
    CommonModule,
    MatTableModule
  ],
  exports:[
    TableMaterialComponent
  ],
})
export class TableMaterialModule { }
