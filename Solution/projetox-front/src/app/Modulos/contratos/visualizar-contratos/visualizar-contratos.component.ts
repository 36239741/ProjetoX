import { Subscription } from 'rxjs';
import { Router } from '@angular/router';
import { ContratoService } from './../../../shared/Services/contrato.service';
import { Component, OnInit, OnDestroy } from '@angular/core';

import {
  ITdDataTableColumn,
} from '@covalent/core/data-table';
import { IPageChangeEvent } from '@covalent/core/paging';
import { TdDialogService } from '@covalent/core/dialogs';
import { ImportComponent } from './import/import.component';
import { PageContrato } from '../../../shared/model/page-contrato';
import { Contrato } from 'src/app/shared/model/Contrato';

const DECIMAL_FORMAT: (v: any) => any = (v: number) => v.toFixed(2);

@Component({
  selector: 'app-visualizar-contratos',
  templateUrl: './visualizar-contratos.component.html',
  styleUrls: ['./visualizar-contratos.component.css']
})
export class VisualizarContratosComponent implements OnInit {

  columns: ITdDataTableColumn[] = [
    { name: 'numero', label: 'No. Contrato' },
    { name: 'nomePaciente', label: 'Nome do Paciente' },
    {
      name: 'valorTotal',
      label: 'Valor Contratado',
      numeric: true,
      format: DECIMAL_FORMAT
    }
  ];
  pageContrato: PageContrato;
  contratos: any[] = [];
  excludedColumnsFilterContrato: string[] = ['nomePaciente', 'valorTotal','id'];
  excludedColumnsFilterNomePaciente: string[] = ['valorTotal', 'numero','id'];
  pageSize: number = 10;
  total: number;
  page: number = 0;
  numero: string = '';
  nomePaciente: string = '';
  numeroContrato: string = '';
  contrato: Contrato;
  findContratoAfterImport: Boolean = false;

  constructor(
    private _dialogService: TdDialogService,
    private contratoService: ContratoService,
    private route: Router  ) {}

  ngOnInit() {
    this.startTable();
  }
  startTable() {
    this.contratoService
      .findAllContratos(this.page, this.pageSize)
      .subscribe(pageContrato => {
          this.pageContrato = pageContrato;
          this.total = pageContrato.totalElements;
          this.page = pageContrato.totalPages;
          this.contratos = this.pageContrato['content'];
      });
  }
  findByFilter(){
    this.contratoService.findByFilters(this.nomePaciente,this.numero, this.page = 0, this.pageSize = 10)
    .subscribe(pageFilter => {
      this.contratos = pageFilter['content'];
    });
  }
  changePageSize(event: IPageChangeEvent){
    this.pageSize = event.pageSize;
    this.page = event.page - 1;
    this.startTable();
  }
  filterContrato(event){
    if(event != ''){
      this.numero = event;
      this.findByFilter();
      this.total = this.contratos.length;
    }
    else{
      this.numero = '';
      this.page = 0;
      this.startTable();
    }
  }

  filterNomePaciente(event){
    if(event != ''){
      this.nomePaciente = event;
      this.findByFilter();
      this.total = this.contratos.length;
    }
    else{
      this.nomePaciente = ''
      this.page = 0;
      this.startTable();
    }

  }
  rowClick(event: any){
    this.numeroContrato = event.row.numero;
    this.route.navigate(['contratos/detalhar/', this.numeroContrato]);
  }


  openModalImport($event) {
    this._dialogService.open(ImportComponent, {
      width: "500px",
      height: "500px",
      disableClose: false
    });
  }

}
