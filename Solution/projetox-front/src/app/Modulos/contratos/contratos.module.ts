import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ContratosComponent } from './contratos.component';
import { ContratosRoutingModule } from './contratos-routing.module';
import { LayoutModule } from '../Layout/layout.module';
import { VisualizarContratosModule } from './visualizar-contratos/visualizar-contratos.module';
import { MatCardModule } from '@angular/material/card';
import { FlexLayoutModule } from '@angular/flex-layout';
import { HttpClientModule } from '@angular/common/http';
import { DetatalharContratosModule } from './detalhar-contratos/detatalhar-contratos.module';
import { CovalentBreadcrumbsModule } from '@covalent/core/breadcrumbs';
import { RouterModule } from '@angular/router';
import { BreadCrumbsModule } from '../../shared/components/bread-crumbs/bread-crumbs.module';
import { NovoServicoModule } from './novo-servico/novo-servico.module';
import { EditarPlanoContratadoModule } from './editar-plano-contratado/editar-plano-contratado.module';

@NgModule({
  declarations: [ContratosComponent],
  imports: [
    CommonModule,
    ContratosRoutingModule,
    LayoutModule,
    VisualizarContratosModule,
    NovoServicoModule,
    MatCardModule,
    FlexLayoutModule,
    HttpClientModule,
    DetatalharContratosModule,
    CovalentBreadcrumbsModule,
    RouterModule,
    BreadCrumbsModule,
    EditarPlanoContratadoModule

  ]
})
export class ContratosModule { }