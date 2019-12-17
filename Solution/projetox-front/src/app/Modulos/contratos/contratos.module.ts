import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ContratosRoutingModule } from './contratos-routing.module';
import { LayoutModule } from '../Layout/layout.module';
import { HttpClientModule } from '@angular/common/http';
import { DetatalharContratosModule } from './detalhar-contratos/detatalhar-contratos.module';
import { RouterModule } from '@angular/router';
import { EditarPlanoContratadoModule } from './editar-plano-contratado/editar-plano-contratado.module';
import { NovoServicoComponent } from './novo-servico/novo-servico.component';
import { VisualizarContratosComponent } from './visualizar-contratos/visualizar-contratos.component';
import { FilterComponent } from './visualizar-contratos/filter/filter.component';
import { ImportFeedBackComponent } from './visualizar-contratos/import/importFeedBack/import-feed-back.component';
import { ImportButtonComponent } from './visualizar-contratos/import/importButton/import-button.component';
import { ImportComponent } from './visualizar-contratos/import/import.component';
import { FormsModule } from '@angular/forms';
import { FormModule } from './form/form.module';
import {MaterialModule} from '../../core/material/material.module';



@NgModule({
  declarations: [
    NovoServicoComponent,
    VisualizarContratosComponent, 
    FilterComponent, 
    ImportFeedBackComponent, 
    ImportButtonComponent, 
    ImportComponent,],
  imports: [
    CommonModule,
    ContratosRoutingModule,
    LayoutModule,
    HttpClientModule,
    DetatalharContratosModule,
    RouterModule,
    EditarPlanoContratadoModule,
    FormsModule,
    FormModule,
    MaterialModule,
    LayoutModule
    
  ],
  entryComponents: [
    ImportComponent
  ],
})
export class ContratosModule { }
