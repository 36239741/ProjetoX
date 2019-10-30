import { FlexLayoutModule } from '@angular/flex-layout';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CovalentDataTableModule } from '@covalent/core/data-table';
import { TabelaServicosComponent } from './tabela-servicos.component';
import { MatSelectModule } from '@angular/material/select';
import {MatIconModule} from '@angular/material/icon';


@NgModule({
  declarations: [TabelaServicosComponent],
  imports: [
    CommonModule,
    CovalentDataTableModule,
    MatSelectModule,
    FlexLayoutModule,
    MatIconModule
    ],
  exports:[
    TabelaServicosComponent
  ]
})
export class TabelaServicosModule { }
