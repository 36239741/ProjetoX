import { Component, OnInit, ViewChild } from '@angular/core';

import { ITdDataTableColumn , TdDataTableService} from '@covalent/core/data-table';
import { IPageChangeEvent, TdPagingBarComponent } from '@covalent/core/paging';
import { MatPaginator } from '@angular/material/paginator';


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
  basicData: any[] = [{
    'contrato': 1,
    'paciente': 'Henrique',
    'valor': 1600.00,
  },
  {
  'contrato': 2,
  'paciente': 'Jose',
  'valor': 1000.00,
},
{
  'contrato': 3,
  'paciente': 'Jose',
  'valor': 1000.00,
},
{
  'contrato': 4,
  'paciente': 'Jose',
  'valor': 1000.00,
},
{
  'contrato': 5,
  'paciente': 'Jose',
  'valor': 1000.00,
},
{
  'contrato': 6,
  'paciente': 'Jose',
  'valor': 1000.00,
},
{
  'contrato': 7,
  'paciente': 'Jose',
  'valor': 1000.00,
},
{
  'contrato': 8,
  'paciente': 'Jose',
  'valor': 1000.00,
},
{
  'contrato': 9,
  'paciente': 'Jose',
  'valor': 1000.00,
},
{
  'contrato': 10,
  'paciente': 'Jose',
  'valor': 1000.00,
},
{
  'contrato': 11,
  'paciente': 'Jose',
  'valor': 1000.00,
},


];
@ViewChild(TdPagingBarComponent, {static: true}) pagingBar: TdPagingBarComponent;
  filterData: any[];
  excludedColumnsFilterContrato: string[] = ["paciente", "valor"];
  excludedColumnsFilterNomePaciente: string[] = ["valor", "contrato"];
  filtroByNumeroContrato: string = '';
  filteredTotal:number;
  currentPage:number = 1;
  fromRow: number = 1
  pageSize: number = 10;
  total:number;
  constructor(private tableService: TdDataTableService) { }

  ngOnInit() {
    this.filterData = this.basicData.slice(0,10);
    this.total = this.filterData.length;
  }
  changePageSize(event: IPageChangeEvent): void {
    this.filterData = this.basicData;
    this.total =this.filterData.length;
    this.filterData = this.tableService.pageData(this.filterData, event.fromRow , event.page * event.pageSize);
  }
   filterContrato(fitroContrato: string){
     this.filterData = this.tableService.filterData(this.basicData, fitroContrato, true, this.excludedColumnsFilterContrato);
     this.total =  this.filterData.length;

  }
  filterNomePaciente(filtroNomePaciente: string){
     this.filterData = this.tableService.filterData(this.basicData, filtroNomePaciente, true, this.excludedColumnsFilterNomePaciente);
     this.total = this.filterData.length;

  }



}
