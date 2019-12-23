import { FormBuilder } from '@angular/forms';
import { FormGroup } from '@angular/forms';
import { Component, OnInit } from '@angular/core';
import { ITdDataTableColumn } from '@covalent/core';

@Component({
  selector: 'app-registros',
  templateUrl: './registros.component.html',
  styleUrls: ['./registros.component.css']
})
export class RegistrosComponent implements OnInit {
  formGorup: FormGroup;
  constructor(private formBuilder: FormBuilder) { }
  pageSize: number;
  total: number;
  columns: ITdDataTableColumn[] = [
    { name: 'horarioEntrada', label: 'Entrada',},
    { name: 'horarioSaida', label: 'Saída',},
    { name: 'tempo', label: 'Tempo'},
    { name: 'situacao',label: 'Situação'},
    { name: 'valorTotal',label: 'Valor total',numeric: true, },
    { name: 'acoes', label: 'Ações'}
  ]
  ngOnInit() {
    this.form();
  }
  form(){
      this.formGorup = this.formBuilder.group({
        afterDate: [],
        beforeDate: []
      });
  }
  changePageSize($event){

  }
}
