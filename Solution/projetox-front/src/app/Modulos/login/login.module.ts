import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LoginComponent } from './login/login.component';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MatButtonModule } from '@angular/material/button';
import { CovalentDynamicFormsModule } from '@covalent/dynamic-forms';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ErrorsModule } from '../../shared/components/errors/errors.module';
import { CovalentMessageModule } from '@covalent/core/message';

@NgModule({
  declarations: [LoginComponent],
  imports: [
    CommonModule,
    CovalentDynamicFormsModule,
    MatButtonModule,
    FlexLayoutModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule,
    ErrorsModule,
    CovalentMessageModule,

  ]
})
export class LoginModule { }
