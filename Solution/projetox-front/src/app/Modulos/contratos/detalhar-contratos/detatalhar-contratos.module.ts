import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FlexLayoutModule } from '@angular/flex-layout';
import { DetatalharContratosComponent } from './detatalhar-contratos.component';
import {MatButtonModule} from '@angular/material/button';
import { TabelaServicosModule } from './tabela-servicos/tabela-servicos.module';
import {MatIconModule} from '@angular/material/icon';
import { CovalentDialogsModule } from '@covalent/core/dialogs';
import {MatFormFieldModule} from '@angular/material/form-field'; 

@NgModule({
  declarations: [DetatalharContratosComponent],
  imports: [
    CommonModule,
    FlexLayoutModule,
    MatButtonModule,
    TabelaServicosModule,
    MatIconModule,
    RouterModule,
    CovalentDialogsModule,
    MatFormFieldModule

  ],
})
export class DetatalharContratosModule { }
