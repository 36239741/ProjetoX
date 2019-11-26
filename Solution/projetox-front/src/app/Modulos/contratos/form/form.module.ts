import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormComponent } from './form.component';
import { MatInputModule } from '@angular/material/input';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MatSelectModule } from '@angular/material/select';
import {MatCheckboxModule} from '@angular/material/checkbox'; 
import { ReactiveFormsModule } from '@angular/forms';
import {MatButtonModule} from '@angular/material/button'; 
import { NgxCurrencyModule } from "ngx-currency";
import { FormCamposOcultosComponent } from './form-campos-ocultos/form-campos-ocultos.component';


@NgModule({
  declarations: [FormComponent, FormCamposOcultosComponent],
  imports: [
    CommonModule,
    MatInputModule,
    FlexLayoutModule,
    MatSelectModule,
    MatCheckboxModule,
    MatButtonModule,
    ReactiveFormsModule,
    NgxCurrencyModule
  ],
  exports: [FormComponent]
})
export class FormModule { }
