import { FlexLayoutModule } from '@angular/flex-layout';
import { ClockBiometriaComponent } from './clock-biometria.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';



@NgModule({
  declarations: [ClockBiometriaComponent],
  imports: [
    CommonModule,
    FlexLayoutModule,
  ],
  entryComponents: [
    ClockBiometriaComponent
  ],
  exports:[
    ClockBiometriaComponent
  ],
})
export class ClockBiometriaModule { }
