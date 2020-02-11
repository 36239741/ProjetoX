import { DialogConfirmModule } from './../../shared/components/dialog-confirm/dialog-confirm.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { HomeComponent } from './home.component';
import { RegistroBiometriaComponent } from './registro-biometria/registro-biometria.component';
import { FormsModule } from '@angular/forms';
import { TableModule } from '../../shared/components/table/table.module';
import { MaterialModule } from 'src/app/core/material/material.module';
import { CovalentDialogsModule } from '@covalent/core/dialogs';
import { DialogConfirmComponent } from 'src/app/shared/components/dialog-confirm/dialog-confirm.component';


@NgModule({
  declarations: [HomeComponent, RegistroBiometriaComponent],
  imports: [
    CommonModule,
    FormsModule,
    TableModule,
    MaterialModule,
    CovalentDialogsModule,
    DialogConfirmModule
  ],
  entryComponents: [DialogConfirmComponent]

})
export class HomeModule { }
