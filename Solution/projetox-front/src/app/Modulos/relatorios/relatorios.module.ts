import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { RelatoriosComponent } from './relatorios.component';
import { RelatorioRoutingModule } from './relatorios-routing.module';
import { LayoutModule } from '../Layout/layout.module';



@NgModule({
  declarations: [RelatoriosComponent],
  imports: [
    CommonModule,
    RelatorioRoutingModule,
    LayoutModule
  ]
})
export class RelatoriosModule { }
