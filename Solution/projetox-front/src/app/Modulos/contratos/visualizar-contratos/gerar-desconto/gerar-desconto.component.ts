import { ToastService } from './../../../../shared/Services/toast.service';
import { ContratoService } from './../../../../shared/Services/contrato.service';
import { FormGroup, FormBuilder } from '@angular/forms';
import { Contrato } from './../../../../shared/model/Contrato';
import { TdDialogService } from '@covalent/core/dialogs';
import { Component, OnInit, Inject, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import {fromEvent } from 'rxjs';
import { debounceTime, distinctUntilChanged, tap, filter } from 'rxjs/operators';
import { MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'app-gerar-desconto',
  templateUrl: './gerar-desconto.component.html',
  styleUrls: ['./gerar-desconto.component.css']
})
export class GerarDescontoComponent implements OnInit, AfterViewInit {


  constructor(private dialogService: TdDialogService,
    @Inject(MAT_DIALOG_DATA) public contrato: Contrato,
    @ViewChild('valorSessao',null) private inputValorSessao: ElementRef,
    private formBuilder: FormBuilder,
    private contratoService: ContratoService,
    private message: ToastService) { }
    
  descontoGerado: Boolean = false;
  formGroup: FormGroup;
  valorDesconto: number = 0;
  returnContrato: Contrato = null;
  ngOnInit() {
      this.form();
  }

  ngAfterViewInit(): void {
    fromEvent(this.inputValorSessao.nativeElement,'keyup')
    .pipe(
        filter(Boolean),
        debounceTime(600),
        distinctUntilChanged(),
        tap((text) => {
          this.valorDesconto = this.formGroup.get('valorDesconto').value;
        })
    )
    .subscribe();
}

form() {
    this.formGroup = this.formBuilder.group({
        valorDesconto: ['']
    });
}

calculoValorContratoAposDesconto() {
    return this.contrato.valorTotal - this.valorDesconto;
}
gerarDesconto() {
    this.contratoService.gerarDesconto(this.contrato.numero, this.valorDesconto).subscribe(contrato => {
        this.returnContrato = contrato;
        this.descontoGerado = true;
    },
    error => {
        this.message.toastError(error.error.message);
    });
}
  
  closeModal(event: any) {
    this.descontoGerado = false;
    this.dialogService.closeAll();
  }
}
