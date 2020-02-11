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

  subscription: Subscription;
  constructor(private contratoService: ContratoService,
              private tostService: ToastService,
              private snackBar: MatSnackBar) { }

  ngOnInit() {
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
