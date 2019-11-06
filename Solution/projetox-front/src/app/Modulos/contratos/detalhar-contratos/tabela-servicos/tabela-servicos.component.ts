import { Component, OnInit, Input } from '@angular/core';

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

  ngOnInit() {
  }

}
