import { Component, OnInit, Input } from '@angular/core';
import { TableMaterialConfig } from './table-material-config';

@Component({
  selector: 'app-table-material',
  templateUrl: './table-material.component.html',
  styleUrls: ['./table-material.component.css']
})
export class TableMaterialComponent implements OnInit {
  displayedColumns: String[] = [];
  @Input() dataSource: any = [];
  @Input() tableMaterialConfig: TableMaterialConfig[] = [];

  constructor() { }

  ngOnInit() {
    this.columnsDisplay();
  }
  rowc(click: any){
    console.log(click);
  }
  columnsDisplay(){
    this.tableMaterialConfig.forEach(data => {
      this.displayedColumns.push(data.name);
    });
  }
}
