import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ImportFeedBackComponent } from './import-feed-back.component';
import { CovalentMessageModule } from '@covalent/core/message';
import { MatIconModule } from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';

@NgModule({
  declarations: [ImportFeedBackComponent],
  imports: [
    CommonModule,
    CovalentMessageModule,
    MatIconModule,
    MatButtonModule
  ],
  exports: [
    ImportFeedBackComponent
  ]
})
export class ImportFeedBackModule { }
