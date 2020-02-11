import { ToastService } from '../../Services/toast.service';
import { ContratoService } from '../../Services/contrato.service';
import { Subscription } from 'rxjs';
import { interval } from 'rxjs';
import { Component, OnInit, OnDestroy, Inject } from '@angular/core';
import { MatSnackBar, MAT_SNACK_BAR_DATA } from '@angular/material';
import { take } from 'rxjs/operators';

@Component({
  selector: 'app-clock-biometria',
  templateUrl: './clock-biometria.component.html',
  styleUrls: ['./clock-biometria.component.css']
})
export class ClockBiometriaComponent implements OnInit {

  constructor(private contratoService: ContratoService,
              private tostService: ToastService,
              private snackBar: MatSnackBar,
              @Inject(MAT_SNACK_BAR_DATA) public message: String) { }

  ngOnInit() {
  }


  cancelCaptureFingerPrint() {
    this.contratoService.cancelCapture().subscribe(() =>
      error => {
          this.snackBar.dismiss();
          this.tostService.toastWarnning(error.error.message);
      });
  }


}
