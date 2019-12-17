import { HomeModule } from './../home/home.module';
import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';


import { LayoutComponent } from './layout.component';
import { HttpClientModule } from '@angular/common/http';
import { LayoutRoutingModule } from './layout.routing';
import { MaterialModule } from 'src/app/core/material/material.module';

@NgModule({
  declarations: [
    LayoutComponent,
  ],
  imports: [
    CommonModule,
    RouterModule,
    HttpClientModule,
    LayoutRoutingModule,
    MaterialModule,
    HomeModule
  ],
  exports:[
    LayoutComponent
  ]
})
export class LayoutModule { }
