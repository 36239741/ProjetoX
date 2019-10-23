import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CovalentDialogsModule } from '@covalent/core/dialogs';
import { ImportButtonComponent } from './import-button.component';
import {MatButtonModule} from '@angular/material/button';


@NgModule({
  declarations: [ImportButtonComponent],
  imports: [
    CommonModule,
    CovalentDialogsModule,
    MatButtonModule
    
  ],
  exports: [
    ImportButtonComponent
  ]
})
export class ImportModuleButton { }
