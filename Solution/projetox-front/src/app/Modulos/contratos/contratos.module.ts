import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ContratosComponent } from './contratos.component';
import { ContratosRoutingModule } from './contratos-routing.module';
import { LayoutModule } from '../Layout/layout.module';



@NgModule({
  declarations: [ContratosComponent],
  imports: [
    CommonModule,
    ContratosRoutingModule,
    LayoutModule
  ]
})
export class ContratosModule { }
