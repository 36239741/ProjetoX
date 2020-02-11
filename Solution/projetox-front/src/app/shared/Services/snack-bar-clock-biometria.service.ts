import { Injectable } from '@angular/core';
import { ClockBiometriaComponent } from '../components/clock-biometria/clock-biometria.component';
import { MatSnackBar } from '@angular/material';

@Injectable({
  providedIn: 'root'
})
export class SnackBarClockBiometriaService {
  biometriaClock: number = 30;
  constructor(private snackBar: MatSnackBar) { }

  openSnackBarClockBiometria(message: String) {
    this.snackBar.openFromComponent(ClockBiometriaComponent, {
        duration: this.biometriaClock * 1000,
        data: message
    });
  }
}
