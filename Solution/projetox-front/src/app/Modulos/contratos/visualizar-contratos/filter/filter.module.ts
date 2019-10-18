import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FilterComponent } from './filter.component';
import { CovalentSearchModule } from '@covalent/core/search';
import {MatIconModule} from '@angular/material/icon';

@NgModule({
  declarations: [FilterComponent,],
  imports: [
    CommonModule,
    CovalentSearchModule,
    MatIconModule,
  ],
  exports: [
    FilterComponent
  ]
})
export class FilterModule { }
