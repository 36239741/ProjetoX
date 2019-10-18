import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

import { MatIconRegistry } from '@angular/material/icon';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-filter',
  templateUrl: './filter.component.html',
  styleUrls: ['./filter.component.css']
})
export class FilterComponent implements OnInit {
  @Input() placeholder = '';
  @Output() filtro = new EventEmitter<string>();

   constructor(
    iconRegistry: MatIconRegistry,
    sanitizer: DomSanitizer,
  ) {
    iconRegistry.addSvgIcon('cancel', sanitizer.bypassSecurityTrustResourceUrl('/assets/icons/cancel.svg'));

  }

  ngOnInit() {

  }

  filterByNumeroContrato($event){
    this.filtro.emit($event);
  }


}
