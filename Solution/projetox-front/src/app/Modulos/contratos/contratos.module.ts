import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ContratosComponent } from './contratos.component';
import { ContratosRoutingModule } from './contratos-routing.module';
import { LayoutModule } from '../Layout/layout.module';
import { VisualizarContratosModule } from './visualizar-contratos/visualizar-contratos.module';
import { MatCardModule } from '@angular/material/card';
import { FlexLayoutModule } from '@angular/flex-layout';
import { HttpClientModule } from '@angular/common/http';


@NgModule({
  declarations: [ContratosComponent, ],
  imports: [
    CommonModule,
    ContratosRoutingModule,
    LayoutModule,
    VisualizarContratosModule,
    MatCardModule,
    FlexLayoutModule,
    HttpClientModule

  ]
})
export class ContratosModule { }
