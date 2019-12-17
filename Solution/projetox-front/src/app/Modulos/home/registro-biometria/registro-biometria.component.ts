import { MatSnackBar } from '@angular/material';
import { ToastService } from 'src/app/shared/Services/toast.service';
import {  Contrato, HorarioEntradaOrHorarioSaida } from 'src/app/shared/model/Contrato';
import { ContratoService } from './../../../shared/Services/contrato.service';
import { Component, OnInit, ViewContainerRef, } from '@angular/core';
import { RegistroService } from '../../../shared/Services/registro.service';
import { SnackBarClockBiometriaService } from '../../../shared/Services/snack-bar-clock-biometria.service';
import { ITdDataTableColumn} from '@covalent/core/data-table';
import { TdDialogService } from '@covalent/core/dialogs';

const DATA_FORMAT: (v: any) => any = (v: HorarioEntradaOrHorarioSaida) => {return ('0' + v.hour).slice(-2) + ':' + ('0' + v.minute).slice(-2)};
const DIAS_FORMAT: (v: any) => any = (v: any) => {var diasSemana =
    v.map(function(v){return v['diasSemana'];
})
return diasSemana};
@Component({
  selector: 'app-registro-biometria',
  templateUrl: './registro-biometria.component.html',
  styleUrls: ['./registro-biometria.component.css']
})
export class RegistroBiometriaComponent implements OnInit {
    columns: ITdDataTableColumn[] = [
        { name: 'tipoContrato', label: 'Tipo do Contrato'},
        { name: 'servico.servico', label: 'Serviço', width: {min:200} },
        { name: 'sessao', label: 'Sessões', },
        { name: 'horarioEntrada', label: 'Entrada Padrão', format: DATA_FORMAT},
        { name: 'horarioSaida', label: 'Saída Padrão', format: DATA_FORMAT},
        { name: 'diaConsulta', label: 'Dias da Semana', width: {min: 300} ,format: DIAS_FORMAT},
        ];
  servico: any;
  data: any[] = [];
  contrato: Contrato;
  hideTable: boolean = false; 
  noDataMessage: String = 'Nenhum serviço encontrado';
   
  constructor(private contratoService: ContratoService,
              private snackBar: MatSnackBar,
              private toastService: ToastService,
              private clockBiometriaService: SnackBarClockBiometriaService,
              private registroService: RegistroService,
              private _dialogService: TdDialogService,
              private _viewContainerRef: ViewContainerRef) { }

  ngOnInit() {
  }
  saveEntrada(){
      this.findContratoByBiometria();
  }
  saveSaida(){
      this.findContratoByBiometria();
      this.registroService.saveHoraSaida(this.contrato.numero);
  }
  openClockBiometria(){
      this.clockBiometriaService.openSnackBarClockBiometria();
  }
  findContratoByBiometria() {
    this.contratoService.findByBiometria().subscribe(contrato => {
        this.snackBar.dismiss();
        this.contrato = contrato;
        this.data = contrato.planoContratado;
        this.hideTable = true;
    },
    error=>{
        this.snackBar.dismiss();
        this.toastService.toastError(error.error.message);
    });
  }
  rowClick(event: any) {
    this.openConfirm(event);
  }
  openConfirm(event: any): void {
    this._dialogService.openConfirm({
      message: 'Confirma o registro de entrana nesse serviço?.',
      disableClose:  false, // defaults to false
      viewContainerRef: this._viewContainerRef, //OPTIONAL
      title: 'Confirmar', //OPTIONAL, hides if not provided
      cancelButton: 'Cancelar', //OPTIONAL, defaults to 'CANCEL'
      acceptButton: 'Confirmar', //OPTIONAL, defaults to 'ACCEPT'
      width: '500px', //OPTIONAL, defaults to 400px
    }).afterClosed().subscribe((accept: boolean) => {
      if (accept) {
        this.registroService.saveHoraEntrada(this.contrato.numero, event.row.id).subscribe(() => {
            this.toastService.toastSuccess('Hora de entrada salva com sucesso.');
            this.hideTable = false;
        },
        error => {
            this.toastService.toastError(error.error.message);
        });
      } else {
      }
    });
  }


}
