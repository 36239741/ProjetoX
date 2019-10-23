import { Component, OnInit, Output, EventEmitter } from "@angular/core";

@Component({
  selector: "app-import-button",
  templateUrl: "./import-button.component.html",
  styleUrls: ["./import-button.component.css"]
})
export class ImportButtonComponent implements OnInit {
  
  @Output() click: EventEmitter<any> = new EventEmitter();

  constructor() {}

  ngOnInit() {}

  clickImport($event) {
    this.click.emit($event);
  }


}
