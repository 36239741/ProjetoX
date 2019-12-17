import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { HomeComponent } from './home.component';
import { RegistroBiometriaComponent } from './registro-biometria/registro-biometria.component';
import { FormsModule } from '@angular/forms';
import { TableModule } from '../../shared/components/table/table.module';
import { MaterialModule } from 'src/app/core/material/material.module';
import { CovalentDialogsModule } from '@covalent/core/dialogs';


@NgModule({
  declarations: [HomeComponent, RegistroBiometriaComponent],
  imports: [
    CommonModule,
    FormsModule,
    TableModule,
    MaterialModule,
    CovalentDialogsModule,
  ],


})
export class HomeModule { }
