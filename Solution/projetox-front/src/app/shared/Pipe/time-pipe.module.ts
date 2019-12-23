import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {TimePipePipe} from './TimePipe.pipe';

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [TimePipePipe],
  exports: [TimePipePipe]
})
export class PipeTimeModule { }
