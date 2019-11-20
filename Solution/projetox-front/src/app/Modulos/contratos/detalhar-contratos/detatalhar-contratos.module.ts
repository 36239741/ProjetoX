import { RouterModule } from '@angular/router';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FlexLayoutModule } from '@angular/flex-layout';
import { DetatalharContratosComponent } from './detatalhar-contratos.component';
import {MatButtonModule} from '@angular/material/button';
import { TabelaServicosModule } from './tabela-servicos/tabela-servicos.module';
import {MatIconModule} from '@angular/material/icon';

@NgModule({
  declarations: [DetatalharContratosComponent],
  imports: [
    CommonModule,
    FlexLayoutModule,
    MatButtonModule,
    TabelaServicosModule,
    MatIconModule,
    RouterModule

  ]
})
export class DetatalharContratosModule { }
