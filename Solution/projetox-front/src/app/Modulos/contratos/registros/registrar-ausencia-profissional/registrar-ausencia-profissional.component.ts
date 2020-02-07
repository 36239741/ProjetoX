import { Registro } from './../../../../shared/model/registro';
import { Component, OnInit, Inject } from '@angular/core';
import { TdDialogService } from '@covalent/core';
import { ToastService } from '../../../../shared/Services/toast.service';
import { RegistroService } from '../../../../shared/Services/registro.service';
import { MAT_DIALOG_DATA } from '@angular/material';





@Component({
  selector: 'app-registrar-ausencia-profissional',
  templateUrl: './registrar-ausencia-profissional.component.html',
  styleUrls: ['./registrar-ausencia-profissional.component.css']
})
export class RegistrarAusenciaProfissionalComponent implements OnInit {
  constructor(private _dialogService: TdDialogService,
    private registroService: RegistroService,
    private toastService: ToastService,
    @Inject(MAT_DIALOG_DATA) public registro: Registro

   ) { }

  ngOnInit() {
  }

  confirmarTrocaDeProfissional() {
    this.registroService.ausenciaProfissional(this.registro.id).subscribe(registro => {
        this.toastService.toastSuccess('Declarada ausência do profissional com sucesso.')
        this._dialogService.closeAll();
       },error => {
           this.toastService.toastError(error.error.message);
           this._dialogService.closeAll();
       })
  }
  close(){
      this._dialogService.closeAll();
  }
}
