import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DialogConfirmComponent } from './dialog-confirm.component';
import { CovalentDialogsModule } from '@covalent/core/dialogs';
import { MatButtonModule } from '@angular/material/button';
import {MatDialogModule} from '@angular/material/dialog';



@NgModule({
  declarations: [DialogConfirmComponent],
  imports: [
    CommonModule,
    CovalentDialogsModule,
    MatButtonModule,
    MatDialogModule
  ],
  exports: [DialogConfirmComponent]
})
export class DialogConfirmModule { }
