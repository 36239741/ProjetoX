import { NovoServicoModule } from './novo-servico/novo-servico.module';
import { TableModule } from './../../shared/components/table/table.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ContratosRoutingModule } from './contratos-routing.module';
import { LayoutModule } from '../Layout/layout.module';
import { HttpClientModule } from '@angular/common/http';
import { DetatalharContratosModule } from './detalhar-contratos/detatalhar-contratos.module';
import { RouterModule } from '@angular/router';
import { EditarPlanoContratadoModule } from './editar-plano-contratado/editar-plano-contratado.module';
import { VisualizarContratosComponent } from './visualizar-contratos/visualizar-contratos.component';
import { FilterComponent } from './visualizar-contratos/filter/filter.component';
import { ImportFeedBackComponent } from './visualizar-contratos/import/importFeedBack/import-feed-back.component';
import { ImportButtonComponent } from './visualizar-contratos/import/importButton/import-button.component';
import { ImportComponent } from './visualizar-contratos/import/import.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FormModule } from './form/form.module';
import { MaterialModule } from '../../core/material/material.module';
import { RegistrosComponent } from './registros/registros.component';
import { PipeTimeModule } from '../../shared/Pipe/time-pipe.module';
import { AlterarServicoComponent } from './registros/alterar-servico/alterar-servico.component';
import { GerarDescontoComponent } from './visualizar-contratos/gerar-desconto/gerar-desconto.component';
import { NgxCurrencyModule } from 'ngx-currency';
import { RegistrarAusenciaProfissionalComponent } from './registros/registrar-ausencia-profissional/registrar-ausencia-profissional.component';



@NgModule({
  declarations: [
    VisualizarContratosComponent, 
    FilterComponent, 
    ImportFeedBackComponent, 
    ImportButtonComponent, 
    ImportComponent,
    RegistrosComponent,
    AlterarServicoComponent,
    GerarDescontoComponent,
    RegistrarAusenciaProfissionalComponent],
  imports: [
    NgxCurrencyModule,
    PipeTimeModule,
    CommonModule,
    ContratosRoutingModule,
    LayoutModule,
    HttpClientModule,
    DetatalharContratosModule,
    RouterModule,
    EditarPlanoContratadoModule,
    FormsModule,
    ReactiveFormsModule,
    FormModule,
    TableModule,
    MaterialModule,
    LayoutModule,
    NovoServicoModule
  ],

  entryComponents: [
    ImportComponent,
    AlterarServicoComponent,
    GerarDescontoComponent,
    RegistrarAusenciaProfissionalComponent
  ],
})
export class ContratosModule { }
