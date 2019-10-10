import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { HomeComponent } from './home.component';
import { LayoutModule } from '../Layout/layout.module';



@NgModule({
  declarations: [HomeComponent],
  imports: [
    CommonModule,
    LayoutModule
  ],
  exports: [
    HomeComponent
  ]
})
export class HomeModule { }
