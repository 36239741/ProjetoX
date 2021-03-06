import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'TimePipe'
})
export class TimePipePipe implements PipeTransform {

  transform(value: number): any {
    const hours = Math.floor(value / 60);
    const minutes = value % 60;
    return ('0' + hours).slice(-2) + ':' + ('0' + minutes).slice(-2);
  }

}
