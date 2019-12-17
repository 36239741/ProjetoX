import { LayoutModule } from './../Layout/layout.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { RelatoriosComponent } from './relatorios.component';
import { RelatorioRoutingModule } from './relatorios-routing.module';



@NgModule({
  declarations: [RelatoriosComponent],
  imports: [
    CommonModule,
    RelatorioRoutingModule,
    LayoutModule

  ]
})
export class RelatoriosModule { }
