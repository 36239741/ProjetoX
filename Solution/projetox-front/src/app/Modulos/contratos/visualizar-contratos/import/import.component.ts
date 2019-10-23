import { Component, OnInit } from "@angular/core";
import { TdDialogService } from "@covalent/core/dialogs";

@Component({
  selector: "app-import",
  templateUrl: "./import.component.html",
  styleUrls: ["./import.component.css"]
})
export class ImportComponent implements OnInit {
  constructor(private _dialogService: TdDialogService) {}

  ngOnInit() {}

  files: any;
  disabled: boolean = false;

  selectEvent(files: FileList | File): void {
    if (files instanceof FileList) {
    } else {
    }
  }

  buttonFechar($event) {
    this._dialogService.closeAll();
  }
}
