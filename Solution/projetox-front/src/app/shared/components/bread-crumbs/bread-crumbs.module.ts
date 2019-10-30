import { FlexLayoutModule } from '@angular/flex-layout';
import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BreadCrumbsComponent } from './bread-crumbs.component';
import { CovalentBreadcrumbsModule } from '@covalent/core/breadcrumbs';
import {MatChipsModule} from '@angular/material/chips';

@NgModule({
  declarations: [BreadCrumbsComponent],
  imports: [
    CommonModule,
    RouterModule,
    CovalentBreadcrumbsModule,
    MatChipsModule,
    FlexLayoutModule
  ],
  exports: [
    BreadCrumbsComponent
  ]
})
export class BreadCrumbsModule { }
