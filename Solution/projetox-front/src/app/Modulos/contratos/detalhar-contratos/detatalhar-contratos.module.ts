import { DialogConfirmModule } from './../../../shared/components/dialog-confirm/dialog-confirm.module';
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
import { MatTooltipModule } from '@angular/material';
import { ClockBiometriaModule } from '../../../shared/components/clock-biometria/clock-biometria.module';
import { DialogConfirmComponent } from 'src/app/shared/components/dialog-confirm/dialog-confirm.component';

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
    MatFormFieldModule,
    MatTooltipModule,
    ClockBiometriaModule,
    DialogConfirmModule

  ],
  entryComponents: [DialogConfirmComponent]
  

})
export class DetatalharContratosModule { }
