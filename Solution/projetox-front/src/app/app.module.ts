import { ClockBiometriaModule } from 'src/app/shared/components/clock-biometria/clock-biometria.module';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule, LOCALE_ID } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginModule } from './Modulos/login/login.module';
import localept from '@angular/common/locales/pt';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { BasicInterceptorService } from './core/interceptor/basic-interceptor.service';
import { registerLocaleData } from '@angular/common';
import { MaterialModule } from './core/material/material.module';
import { CovalentLoadingModule } from '@covalent/core/loading';
import {MatDialogModule} from '@angular/material/dialog';


registerLocaleData(localept, 'pt');

@NgModule({
  declarations: [
    AppComponent,
    ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    LoginModule,
    HttpClientModule,
    MaterialModule,
    CovalentLoadingModule,
    ClockBiometriaModule,
    MatDialogModule
  ],
  providers: [{
    provide: HTTP_INTERCEPTORS ,
    useClass: BasicInterceptorService,
    multi: true
  },
  { provide: LOCALE_ID, useValue: 'pt' }],
  bootstrap: [AppComponent]
})
export class AppModule { }
