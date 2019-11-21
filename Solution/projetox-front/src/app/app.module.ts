import { BrowserModule } from '@angular/platform-browser';
import { NgModule, LOCALE_ID } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginModule } from './Modulos/login/login.module';
import { HomeModule } from './Modulos/home/home.module';
import localept from '@angular/common/locales/pt';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { CovalentLoadingModule } from '@covalent/core/loading';
import { BasicInterceptorService } from './core/interceptor/basic-interceptor.service';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import { registerLocaleData } from '@angular/common';
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
    HomeModule,
    HttpClientModule,
    CovalentLoadingModule,
    MatProgressSpinnerModule


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
