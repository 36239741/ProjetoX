import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FlexLayoutModule } from '@angular/flex-layout';

import { CovalentDynamicFormsModule } from '@covalent/dynamic-forms';
import {
  CovalentBreadcrumbsModule,
  CovalentDataTableModule,
  CovalentSearchModule,
  CovalentDialogsModule,
  CovalentFileModule,
  CovalentPagingModule,
  CovalentMessageModule,
  CovalentLoadingModule,
  CovalentLayoutModule,
   } from '@covalent/core';


import {
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatFormFieldModule,
    MatCardModule,
    MatTooltipModule,
    MatSnackBarModule,
    MatRadioModule,
    MatProgressSpinnerModule,
    MatListModule,
    MatSidenavModule,
    MatDatepickerModule,
    MatToolbarModule,
    MatNativeDateModule,
    } from '@angular/material';

  import { MatMomentDateModule } from '@angular/material-moment-adapter';



@NgModule({
  imports: [
   /* Angular Material */
    CommonModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatFormFieldModule,
    MatCardModule,
    MatTooltipModule,
    MatSelectModule,
    MatListModule,
    MatButtonModule,
    MatSnackBarModule,
    MatToolbarModule,
    MatSidenavModule,
    MatDatepickerModule,
    MatMomentDateModule,
    MatNativeDateModule,

    /*Teradata Covalent*/

    CovalentBreadcrumbsModule,
    CovalentDataTableModule,
    CovalentSearchModule,
    CovalentDialogsModule,
    CovalentFileModule,
    CovalentPagingModule,
    CovalentLoadingModule,
    CovalentMessageModule,
    CovalentDynamicFormsModule,
    CovalentLayoutModule,

    /*Flex Layout Module*/

    FlexLayoutModule

  ],
  exports: [
       /* Angular Material */
       CommonModule,
       MatSelectModule,
       MatButtonModule,
       MatIconModule,
       MatInputModule,
       MatFormFieldModule,
       MatCardModule,
       MatTooltipModule,
       MatSelectModule,
       MatButtonModule,
       MatSnackBarModule,
       MatRadioModule,
       MatListModule,
       MatToolbarModule,
       MatProgressSpinnerModule,
       MatSidenavModule,
       MatDatepickerModule,
       MatNativeDateModule,
       MatMomentDateModule,


       /*Teradata Covalent*/

       CovalentBreadcrumbsModule,
       CovalentDataTableModule,
       CovalentSearchModule,
       CovalentDialogsModule,
       CovalentFileModule,
       CovalentPagingModule,
       CovalentMessageModule,
       CovalentLoadingModule,
       CovalentDynamicFormsModule,
       CovalentLayoutModule,


    /*Flex Layout Module*/

       FlexLayoutModule
  ],
  declarations: []
})
export class  MaterialModule { }
