import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CovalentLayoutModule } from '@covalent/core/layout';
import { MatListModule } from '@angular/material/list';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { FlexLayoutModule } from '@angular/flex-layout';
import { LayoutComponent } from './layout.component';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
  declarations: [
    LayoutComponent,
  ],
  imports: [
    CommonModule,
    CovalentLayoutModule,
    MatListModule,
    MatButtonModule,
    MatIconModule,
    FlexLayoutModule,
    RouterModule,
    HttpClientModule
  ],
  exports:[
    LayoutComponent
  ]
})
export class LayoutModule { }
