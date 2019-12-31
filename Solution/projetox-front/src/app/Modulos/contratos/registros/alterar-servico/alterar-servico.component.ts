import { Validators } from '@angular/forms';
import { ToastService } from 'src/app/shared/Services/toast.service';
import { RegistroService } from './../../../../shared/Services/registro.service';
import { Servico } from './../../../../shared/model/Contrato';
import { Component, OnInit, Inject, AfterViewInit, ElementRef, ViewChild } from '@angular/core';
import { TdDialogService } from '@covalent/core';
import { MAT_DIALOG_DATA } from '@angular/material';
import { fromEvent } from 'rxjs';
import { debounceTime, filter, distinctUntilChanged, tap } from 'rxjs/operators';
import { FormGroup, FormBuilder } from '@angular/forms';
import { ServicesService } from 'src/app/shared/Services/services.service';
import { Registro } from 'src/app/shared/model/registro';

@Component({
  selector: 'app-alterar-servico',
  templateUrl: './alterar-servico.component.html',
  styleUrls: ['./alterar-servico.component.css']
})
export class AlterarServicoComponent implements OnInit, AfterViewInit {

    selected: String = '';
    formGroup: FormGroup;
    valorSessao: number = 0;
    servicos: Servico [] = [];
    returnRegistro: Registro = null;
    trocaServicoSucesso: Boolean = false;
  constructor(private _dialogService: TdDialogService,
    @ViewChild('valorSessao',null) private inputValorDesconto: ElementRef,
    @Inject(MAT_DIALOG_DATA) public registro: Registro,
    private formBuilder: FormBuilder,
    private registroService: RegistroService,
    private servicoService: ServicesService,
    private toastService: ToastService) {}


  ngOnInit() { 
      this.form();
      this.findAllServicos();
   }

   ngAfterViewInit(): void {
    fromEvent(this.inputValorDesconto.nativeElement,'keyup')
    .pipe(
        filter(Boolean),
        debounceTime(600),
        distinctUntilChanged(),
        tap((text) => {
          this.valorSessao = this.formGroup.get('valorSessao').value;
        })
    )
    .subscribe();
}
findAllServicos() {
    this.servicoService.findAll().subscribe(servico => {
        this.servicos.push(servico);
    });
}
form() {
    this.formGroup = this.formBuilder.group({
        valorSessao: ['', [Validators.required]],
        servico: [this.selected, [Validators.required]]
    });
}


trocarServico() {
    if( this.formGroup.valid) {
        this.registroService.trocaServico('TROCA_DE_SERVICO', this.registro.id, this.selected, this.formGroup.get('valorSessao').value)
        .subscribe(response => {
        this.returnRegistro = response;
        this.trocaServicoSucesso = true;
        },
        error => {
            this.toastService.toastError(error.error.message);
        });
    }

}

  closeModal(event: any){
    if(event) {
        this.trocaServicoSucesso = false
        this._dialogService.closeAll();
    }
  }

}
