import { ContratoService } from './../../../shared/Services/contrato.service';
import { ToastService } from './../../../shared/Services/toast.service';
import { ActivatedRoute } from '@angular/router';
import { RegistroService } from './../../../shared/Services/registro.service';
import { FormGroup, FormBuilder } from '@angular/forms';
import { Component, OnInit, ViewContainerRef } from '@angular/core';
import { ITdDataTableColumn, IPageChangeEvent, TdDialogService } from '@covalent/core';
import { AlterarServicoComponent } from './alterar-servico/alterar-servico.component';
import {Situacao} from '../../../shared/Enum/Situacao';
import { MatDialogConfig } from '@angular/material';
import { Registro } from 'src/app/shared/model/registro';
import { saveAs } from 'file-saver';
import { RegistrarAusenciaProfissionalComponent } from './registrar-ausencia-profissional/registrar-ausencia-profissional.component';



const DECIMAL_FORMAT: (v: any) => any = (v: number) => new Intl.NumberFormat('pt-BR',{style: 'currency', currency:'BRL'} ).format(v);
const DATA_FORMAT: (v: any) => any = (v: any) => 
{if(v != null){

    return  ('0' + v['dayOfMonth']).slice(-2) + '-' +('0' + v['monthValue']).slice(-2) + '-' + v['year']
    +  ' ' +  ('0' + v['hour']).slice(-2)
    + ':' + ('0' + v['minute']).slice(-2);
}else{
    return v = '';
}};
const TIME_FORMAT: (v: any) => any = (v: any) => {
    if(v != null){
        return ('0' + v['hour']).slice(-2) + ':' + ('0' + v['minute']).slice(-2)
    }
    else {
        return '';
    }
   };
const SITUACAO_FORMAT: (v: any) => any = (v: any) => {return Situacao[v]};

@Component({
  selector: 'app-registros',
  templateUrl: './registros.component.html',
  styleUrls: ['./registros.component.css']
})
export class RegistrosComponent implements OnInit {


  constructor(private registroService: RegistroService,
              private contratoService: ContratoService,
              private activatedRoute : ActivatedRoute,
              private toastService : ToastService,
              private formBuilder: FormBuilder,
              private _dialogService: TdDialogService,
            private _viewContainerRef: ViewContainerRef,
    ) { }
    formGroup: FormGroup;
  pageSize: number = 10;
  totalElements: number = 0;
  page: number = 0;
  data: any; 
  numeroContrato: String = '';
  columns: ITdDataTableColumn[] = [
    { name: 'dataHoraEntrada', label: 'Entrada', format: DATA_FORMAT},
    { name: 'dataHoraSaida', label: 'Saída', format: DATA_FORMAT},
    { name: 'tempoTotal', label: 'Tempo (hh:mm)',format: TIME_FORMAT},
    { name: 'situacao',label: 'Situação', format: SITUACAO_FORMAT},
    { name: 'valorTotal', label: 'Valor total', numeric: true, format: DECIMAL_FORMAT },
    { name: 'acoes', label: 'Ações', width: 150, numeric: true}
  ]
  ngOnInit() {
    this.startTable();
    this.form();
    this.findContratoById();
  }
  startTable() {
    this.registroService.findAllRegistro( this.activatedRoute.snapshot.params.id, this.page , this.pageSize).subscribe(data => {
        this.data = data['content'];
        this.totalElements = data['totalElements'];
    });
  }


exportRegistros() {
    this.registroService.exportPlanilhaRegistros(this.activatedRoute.snapshot.params.id).subscribe(planilhaRegistro => {
        let date: Date = new Date();
        let file = new Blob([planilhaRegistro], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });            
        var fileURL = URL.createObjectURL(file);
        saveAs(fileURL, 'registros' + '_' + this.numeroContrato + '_' + 
        date.getDate() + '-'
         + date.getMonth().toString() + 1
          + '-' + 
          date.getFullYear().toString() + '-' +
        date.getHours().toString() + date.getMinutes().toString() + '.xlsx');
    }, error => {
        console.log(error.error.message);
    });
}

findContratoById() {
    this.contratoService.findByContrato(this.activatedRoute.snapshot.params.id).subscribe(contrato => {
        this.numeroContrato = contrato.numero;
    },error => {
        this.toastService.toastError(error.error.message);
    });
}

  changePageSize(event: IPageChangeEvent) {
    this.pageSize = event.pageSize;
    this.page = event.page - 1;
    this.startTable();
  }
  form() {
      this.formGroup = this.formBuilder.group({
        beforeDate: [''],
        afterDate: ['']
      });
  }
  resetDate() {
      this.startTable();
      this.formGroup.get('beforeDate').setValue('');
      this.formGroup.get('afterDate').setValue('');

  }
  filterByData() {
      let dataInicial: String = this.formGroup.get('beforeDate').value;
      let dataFinal: String = this.formGroup.get('afterDate').value;
      let contratoId : String = this.activatedRoute.snapshot.params.id;
      this.registroService.findByDate(dataInicial, dataFinal, contratoId , this.page , this.pageSize).subscribe(registro => {
        this.totalElements = registro['totalElements'];
        this.data = registro['content'];
      },
      error => {
          this.toastService.toastError(error.error.message);
      });
  }

  trocaServico(trocaServico: any) {
    this.openTrocaServico(trocaServico);
  }

  openTrocaServico(tableRow: any) {

    let dialogConfig: MatDialogConfig<any> = {
        width: '650px',
        height: '250px',
        data: tableRow
    };

    if(tableRow) {
      this._dialogService.open(AlterarServicoComponent, dialogConfig).afterClosed().subscribe(() => {
          this.startTable();
      });
    }
  }


  trocaProfissional(trocaProfissional: Registro) {
    this.confirmTrocaDeProfissional(trocaProfissional);
  }

  confirmTrocaDeProfissional(tableRow: Registro): void {
      this.gerarDesconto(tableRow);

  }

    gerarDesconto(registro: Registro) {
        let dialogConfig: MatDialogConfig<any> = {
            width: "650px",
            height: "250px",
            data: registro
        };
        this._dialogService.open(RegistrarAusenciaProfissionalComponent, dialogConfig).afterClosed().subscribe(() => {
            this.startTable();
        });
    }

}