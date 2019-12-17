import { ToastService } from '../../Services/toast.service';
import { ContratoService } from '../../Services/contrato.service';
import { Subscription } from 'rxjs';
import { interval } from 'rxjs';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { MatSnackBar } from '@angular/material';
import { take } from 'rxjs/operators';

@Component({
  selector: 'app-clock-biometria',
  templateUrl: './clock-biometria.component.html',
  styleUrls: ['./clock-biometria.component.css']
})
export class ClockBiometriaComponent implements OnInit, OnDestroy {

  tempoDeCapturaBiometria: number = 30;
  subscription: Subscription;
  timerClock: number;
  constructor(private contratoService: ContratoService,
              private tostService: ToastService,
              private snackBar: MatSnackBar) { }

  ngOnInit() {
      this.timer();
  }
  timer(){
      const number = interval(1000);
      const takeForNumbers = number.pipe(take(this.tempoDeCapturaBiometria));
      this.subscription = takeForNumbers.subscribe(timer => {this.timerClock = timer});
  }

  cancelCaptureFingerPrint() {
      this.contratoService.cancelCapture().subscribe(() =>
      error => {
          this.snackBar.dismiss();
          this.tostService.toastWarnning(error.error.message);
      });
  }

ngOnDestroy(): void {
this.subscription.unsubscribe();
}
}
