
import { ITdDataTableColumn,  } from '@covalent/core/data-table';
import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.css']
})
export class TableComponent implements OnInit {
  @Input() clickable: boolean = false;
  @Input() data: any = [];
  @Input() columns: ITdDataTableColumn;
  @Input() message: String;
  @Output() eventRowClick: EventEmitter<any> = new EventEmitter();

  constructor() {
   }

  ngOnInit() {
  }

  rowClick(event: any) {
    this.eventRowClick.emit(event);
  }

}
