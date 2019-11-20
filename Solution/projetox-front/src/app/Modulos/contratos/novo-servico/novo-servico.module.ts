import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NovoServicoComponent } from './novo-servico.component';
import { FormModule } from '../form/form.module';
import { CovalentDialogsModule } from '@covalent/core/dialogs';

@NgModule({
  declarations: [NovoServicoComponent],
  imports: [
    CommonModule,
    FormModule,
    CovalentDialogsModule
  ]
})
export class NovoServicoModule { }
