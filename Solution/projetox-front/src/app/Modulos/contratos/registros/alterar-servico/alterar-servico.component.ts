import { Servico } from './../../../../shared/model/Contrato';
import { Component, OnInit, Inject } from '@angular/core';
import { TdDialogService } from '@covalent/core';
import { MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'app-alterar-servico',
  templateUrl: './alterar-servico.component.html',
  styleUrls: ['./alterar-servico.component.css']
})
export class AlterarServicoComponent implements OnInit {

  constructor(private _dialogService: TdDialogService,
    @Inject(MAT_DIALOG_DATA) public servicos: Servico[]) {}


  ngOnInit() { 
   }

  cancelar(event: any){
    if(event) {
        this._dialogService.closeAll();
    }
  }

}
