
import { ITdDataTableColumn,  } from '@covalent/core/data-table';
import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.css']
})
export class TableComponent implements OnInit {
  @Input() clickable: boolean;
  @Input() data: any = [];
  @Input() columns: ITdDataTableColumn;
  @Input() message: String;
  @Input() sortOrder: String;
  @Input() sortBy: String;
  @Output() eventRowClick: EventEmitter<any> = new EventEmitter();
  @Output() eventTrocaServico: EventEmitter<any> = new EventEmitter();
  @Output() eventAusenciaProfissional: EventEmitter<any> = new EventEmitter();
  @Output() eventSort: EventEmitter<any> = new EventEmitter();

  constructor() {
   }

  ngOnInit() {
  }

  trocaServico(event: any){
    this.eventTrocaServico.emit(event);
  }
  ausenciaProfissional(event: any){
    this.eventAusenciaProfissional.emit(event);
  }


  sort(event: any) {
    this.eventSort.emit(event);
  }

  rowClick(event: any) {
    this.eventRowClick.emit(event);
  }

}
