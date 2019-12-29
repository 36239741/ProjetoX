import { ActivatedRoute } from '@angular/router';
import { RegistroService } from './../../../shared/Services/registro.service';
import { FormBuilder } from '@angular/forms';
import { FormGroup } from '@angular/forms';
import { Component, OnInit, ViewContainerRef } from '@angular/core';
import { ITdDataTableColumn, IPageChangeEvent, TdDialogService } from '@covalent/core';
import { AlterarServicoComponent } from './alterar-servico/alterar-servico.component';
import {Situacao} from '../../../shared/Enum/Situacao';
import { MatDialogConfig } from '@angular/material';
import { ServicesService } from 'src/app/shared/Services/services.service';
import { Servico } from 'src/app/shared/model/Contrato';


const DECIMAL_FORMAT: (v: any) => any = (v: number) => new Intl.NumberFormat('pt-BR',{style: 'currency', currency:'BRL'} ).format(v);
const DATA_FORMAT: (v: any) => any = (v: any) => 
{if(v != null){
    return  v['dayOfMonth'] + '-' 
    + v['monthValue'] + '-' 
    + v['year'] +  ' ' +  ('0' + v['hour']).slice(-2)
    + ':' + ('0' + v['minute']).slice(-2)
}else{
    return v = '';
}}
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

  constructor(private formBuilder: FormBuilder,
              private registroService: RegistroService,
              private activatedRoute : ActivatedRoute,
              private _dialogService: TdDialogService,
              private servicoService: ServicesService,
              private _viewContainerRef: ViewContainerRef
    ) { }
formGorup: FormGroup;
  pageSize: number = 10;
  totalElements: number = 0;
  page: number = 0;
  data: any; 
  servico:Servico[] = [];
  columns: ITdDataTableColumn[] = [
    { name: 'dataHoraEntrada', label: 'Entrada', format: DATA_FORMAT},
    { name: 'dataHoraSaida', label: 'Saída', format: DATA_FORMAT},
    { name: 'tempoTotal', label: 'Tempo',format: TIME_FORMAT},
    { name: 'situacao',label: 'Situação', format: SITUACAO_FORMAT},
    { name: 'valorTotal', label: 'Valor total', numeric: true, format: DECIMAL_FORMAT },
    { name: 'acoes', label: 'Ações', width: 150, numeric: true}
  ]
  ngOnInit() {
    this.form();
    this.startTable();
  }
  startTable() {
    this.registroService.findAllRegistro( this.activatedRoute.snapshot.params.id, this.page , this.pageSize).subscribe(data => {
        this.data = data['content'];
        this.totalElements = data ['totalElements'];
    });
  }
  form(){
      this.formGorup = this.formBuilder.group({
        afterDate: [],
        beforeDate: []
      });
  }
  changePageSize(event: IPageChangeEvent) {
    this.pageSize = event.pageSize;
    this.page = event.page - 1;
    this.startTable();
  }

  trocaServico(trocaServico: any){
    this.openTrocaServico(trocaServico);
    this.findAllService();
  }

  openTrocaServico(tableRow: any){

    let dialogConfig: MatDialogConfig<any> = {
        width: '50%',
        height: '70%',
        data: this.servico
    };

    if(tableRow) {
        this._dialogService.open(AlterarServicoComponent,dialogConfig);
    }
  }

  findAllService(){
    this.servicoService.findAll().subscribe(servicos => {
      this.servico.push(servicos);
    });
}

  trocaProfissional(trocaProfissional: any) {
    this.confirmTrocaDeProfissional(trocaProfissional);
  }

  confirmTrocaDeProfissional(tableRow: any): void {
    this._dialogService.openConfirm({
      message: 'Tem certeza que deseja registrar a ausência do profissional? \n Sua ação não poderá ser desfeita.',
      disableClose:  false, // defaults to false
      viewContainerRef: this._viewContainerRef, //OPTIONAL
      title: 'Confirmar', //OPTIONAL, hides if not provided
      cancelButton: 'Cancelar', //OPTIONAL, defaults to 'CANCEL'
      acceptButton: 'Aceitar', //OPTIONAL, defaults to 'ACCEPT'
      width: '50%', //OPTIONAL, defaults to 400px
    }).afterClosed().subscribe((accept: boolean) => {
      if (accept) {
        // DO SOMETHING
      } else {
        // DO SOMETHING ELSE
      }
    });
  }

}
