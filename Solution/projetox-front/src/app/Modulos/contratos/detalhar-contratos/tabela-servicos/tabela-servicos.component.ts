import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

import { ITdDataTableColumn } from '@covalent/core/data-table';


@Component({
  selector: 'app-tabela-servicos',
  templateUrl: './tabela-servicos.component.html',
  styleUrls: ['./tabela-servicos.component.css'],
  preserveWhitespaces: true,
})
export class TabelaServicosComponent implements OnInit {


  constructor() { }
  @Input() columns: ITdDataTableColumn[] = [];
  @Input() data: any[] = [];
  @Output() editar:EventEmitter<any> = new EventEmitter();
  @Output() deletar:EventEmitter<any> = new EventEmitter();
  ngOnInit() {
  }
  clickEditar(event){
    this.editar.emit(event);
  }
  clickDeletar(event){
    this.deletar.emit(event);
  }
}
