import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ImportComponent } from './import.component';
import { CovalentFileModule } from '@covalent/core/file';
import { MatIconModule } from '@angular/material/icon'; 
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field'; 
import { MatInputModule } from '@angular/material/input';
import { FlexLayoutModule } from '@angular/flex-layout';
import {MatButtonModule} from '@angular/material/button';

@NgModule({
  declarations: [ImportComponent],
  imports: [
    CommonModule,
    CovalentFileModule,
    MatIconModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    FlexLayoutModule,
    MatButtonModule
  ],
  entryComponents:[
    ImportComponent
  ],
  exports:[
    ImportComponent
  ]
})
export class ImportModule { }
