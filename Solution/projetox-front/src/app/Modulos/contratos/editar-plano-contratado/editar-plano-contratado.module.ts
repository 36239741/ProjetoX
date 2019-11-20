import { FormModule } from './../form/form.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EditarPlanoContratadoComponent } from './editar-plano-contratado.component';
import { CovalentDialogsModule } from '@covalent/core/dialogs';



@NgModule({
  declarations: [EditarPlanoContratadoComponent],
  imports: [
    CommonModule,
    FormModule,
    CovalentDialogsModule
  ]
})
export class EditarPlanoContratadoModule { }
