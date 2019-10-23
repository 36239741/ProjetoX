import { Contrato } from './../../../shared/model/Contrato';
import { ContratoService } from './../../../shared/Services/contrato.service';
import { Component, OnInit, ViewChild } from '@angular/core';

import { ITdDataTableColumn , TdDataTableService} from '@covalent/core/data-table';
import { IPageChangeEvent, TdPagingBarComponent } from '@covalent/core/paging';
import { TdDialogService } from '@covalent/core/dialogs';
import { ImportComponent } from './import/import.component';
import { Observable } from 'rxjs';


const DECIMAL_FORMAT: (v: any) => any = (v: number) => v.toFixed(2);

@Component({
  selector: 'app-visualizar-contratos',
  templateUrl: './visualizar-contratos.component.html',
  styleUrls: ['./visualizar-contratos.component.css']
})
export class VisualizarContratosComponent implements OnInit {

  columns: ITdDataTableColumn[] = [
    { name: 'contrato',  label: 'No. Contrato' },
    { name: 'paciente', label: 'Nome do Paciente' },
    { name: 'valor', label: 'Valor Contratado', numeric: true, format: DECIMAL_FORMAT},
  ];
  basicData: Observable<Contrato>;

@ViewChild(TdPagingBarComponent, {static: true}) pagingBar: TdPagingBarComponent;
  filterData: Observable<Contrato>
  excludedColumnsFilterContrato: string[] = ["paciente", "valor"];
  excludedColumnsFilterNomePaciente: string[] = ["valor", "contrato"];
  filtroByNumeroContrato: string = '';
  filteredTotal: number;
  pageSize: number = 10;
  total:number;
  fromRow: number;
  page: number;
  size: number;

  constructor(private tableService: TdDataTableService,
              private _dialogService: TdDialogService,
              private contratoService: ContratoService) { }

  ngOnInit() {
   this.startTable();
  }
  startTable() {
    this.basicData = this.contratoService.findAllContratos(0,this.pageSize);
    console.log(this.basicData.subscribe());

  }
  //changePageSize(event: IPageChangeEvent): void {
    //this.fromRow = event.fromRow;
    //this.page = event.page;
    //this.size = event.pageSize;
    //this.filterData = this.basicData;
    //this.total =this.filterData.length;
    //this.filterData = this.tableService.pageData(this.filterData, event.fromRow , event.page * event.pageSize);
  //}
   //filterContrato(fitroContrato: string){
     //this.filterData = this.tableService.filterData(this.basicData, fitroContrato, true, this.excludedColumnsFilterContrato);
     //this.total =  this.filterData.length;
  //}
  //filterNomePaciente(filtroNomePaciente: string){
     //this.filterData = this.tableService.filterData(this.basicData, filtroNomePaciente, true, this.excludedColumnsFilterNomePaciente);
     //this.total = this.filterData.length;

  //}
  openModalImport($event){
    this._dialogService.open(ImportComponent,{
      width: "500px",
      height: "500px",
      disableClose:false

    });
  }



}
