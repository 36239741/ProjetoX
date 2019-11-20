import { PlanoContratadoService } from 'src/app/shared/Services/plano-contratado.service';
import { ActivatedRoute, Router } from '@angular/router';
import { DiaConsulta } from './../../../shared/model/dia-consulta';
import { HorarioEntradaOrHorarioSaida } from './../../../shared/model/Contrato';
import { FormPlanoContratado } from './../../../shared/model/formPlanoContratado';
import { Subscription } from 'rxjs';
import { Component, OnInit, OnDestroy, ViewContainerRef } from '@angular/core';
import { BehaviorPlanoContratadoService } from 'src/app/shared/Services/behavior-plano-contratado.service';
import { PlanoContratado } from 'src/app/shared/model/plano-contradao';
import { TdDialogService } from '@covalent/core/dialogs';

@Component({
  selector: 'app-editar-plano-contratado',
  templateUrl: './editar-plano-contratado.component.html',
  styleUrls: ['./editar-plano-contratado.component.css']
})
export class EditarPlanoContratadoComponent implements OnInit, OnDestroy {
  planoContratado?: PlanoContratado;
  formPlanoContratado: FormPlanoContratado = new FormPlanoContratado() ;
  subscription: Subscription;
  rota: String;
  constructor(private behaviorPlanoContratado: BehaviorPlanoContratadoService,
              private activeRoute: ActivatedRoute,
              private router: Router,
              private planoContratadoService: PlanoContratadoService,
              private _dialogService: TdDialogService,
              private _viewContainerRef: ViewContainerRef ) { 
                this.activeRoute.params.subscribe(data =>{
                    this.rota = '/contratos/' + data.id;
                });
              }

  ngOnInit() {
      this.getInformacoesContratos();
      this.form();

  }
  getInformacoesContratos(){
      this.subscription = this.behaviorPlanoContratado.getBehaviorView().subscribe(data => {
      if(data.servico != undefined){
        this.planoContratado = data;
      }
      else{
          this.router.navigate([this.rota]);
      }
     
      });
  }
  mapHorarioEntradaOrSaida(data: HorarioEntradaOrHorarioSaida){
    let hour: String = new String(data.hour);
    let minute: String =  new String (data.minute);
    let horarios: String = ('0' + hour).slice(-2) + ':' + ('0' + minute).slice(-2)
    return horarios;
  }
  mapDiaConsulta(diaConsulta: DiaConsulta[]){
      let diasSemana: String[] = [];
      diaConsulta.forEach(dias =>{
        if (dias.diasSemana === 'SEGUNDA'){
            diasSemana.push('SEG');
        }
        else if (dias.diasSemana === 'TERCA'){
            diasSemana.push('TER');
        }
        else if (dias.diasSemana === 'QUARTA'){
            diasSemana.push('QUA');
        }
        else if (dias.diasSemana === 'QUINTA'){
            diasSemana.push('QUI');
        }
        else if (dias.diasSemana === 'SEXTA'){
            diasSemana.push('SEX');
        }

      });
      return diasSemana;
  }
  form() {
        this.formPlanoContratado.id = this.planoContratado.id;
        this.formPlanoContratado.servico = this.planoContratado.servico.servico;
        this.formPlanoContratado.sessao = this.planoContratado.sessao;
        this.formPlanoContratado.tipoContrato = this.planoContratado.tipoContrato;
        this.formPlanoContratado.valorPlano = this.planoContratado.valorPlano;
        this.formPlanoContratado.valorTotal = this.planoContratado.valorTotal;
        this.formPlanoContratado.horarioEntrada = this.mapHorarioEntradaOrSaida(this.planoContratado.horarioEntrada);
        this.formPlanoContratado.horarioSaida = this.mapHorarioEntradaOrSaida(this.planoContratado.horarioSaida);
        this.formPlanoContratado.diaConsulta = this.mapDiaConsulta(this.planoContratado.diaConsulta);
  }
  recuperarForm(event: any){
    this.planoContratadoService.updatePlanoContratado(event).subscribe(data => {
        this.openAlert('Sucesso', 'Plano Contratado Atualizado com sucesso !!');
     },
        err => {
        this.openAlert(err.error.message, "Error");
        
    });
  }
  
  openAlert(mensagem: string, titulo: string): void {
    this._dialogService.openAlert({
      message: mensagem,
      disableClose: false, // defaults to false
      viewContainerRef: this._viewContainerRef, //OPTIONAL
      title: titulo, //OPTIONAL, hides if not provided
      closeButton: 'Close', //OPTIONAL, defaults to 'CLOSE'
      width: '400px', //OPTIONAL, defaults to 400px
    });
  }

  ngOnDestroy(): void {
      this.subscription.unsubscribe();
    }
  
}
