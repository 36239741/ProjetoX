import { Subscription } from "rxjs";
import { ActivatedRoute, Router } from "@angular/router";
import {Servico, HorarioEntradaOrHorarioSaida} from "./../../../shared/model/Contrato";
import * as moment from 'moment';
import {
    Component,
    OnInit,
    OnDestroy,
    EventEmitter,
    Input,
    Output,
    AfterViewChecked,
    ChangeDetectorRef
} from "@angular/core";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { MatCheckboxChange } from "@angular/material/checkbox";
import { FormPlanoContratado } from "../../../shared/model/formPlanoContratado";
import { BehaviorInformacoesContratoService } from "../../../shared/Services/behavior-informacoes-contrato.service";
import { FormOculto } from "./form-campos-ocultos/form-oculto";
import { ConfigParametro } from 'src/app/shared/model/config-parametros';
import { isEmpty } from 'rxjs/operators';
@Component({
    selector: "app-form",
    templateUrl: "./form.component.html",
    styleUrls: ["./form.component.css"]
})
export class FormComponent implements OnInit, OnDestroy, AfterViewChecked {
    checkBox = [];
    @Input() planoDisable: boolean;
    @Input() servicoDisable: boolean;
    @Output() event: EventEmitter<any> = new EventEmitter<any>();
    @Input() planoContratado?: FormPlanoContratado;
    @Input() rota: String;
    configParametros: ConfigParametro = new ConfigParametro();
    tipoPlano = { Plano: "PLANO", Particular: "PARTICULAR" };
    subscription: Subscription;
    form: FormGroup;
    diasSemanaValidator: boolean;
    horarioEntrada: String;
    horarioSaida: String;
    diasSemana: String[] = [];
    diasSemanaObject: Object;
    services: Servico[];
    valorTotal: String;
    detalhesContrato: String[] = [];
    formOculto: FormOculto[] = [];
    constructor(
        private formBuilder: FormBuilder,
        private activedRoute: ActivatedRoute,
        private behaviorInformacoesContrato: BehaviorInformacoesContratoService,
        private router: Router,
        private cd: ChangeDetectorRef,
    ) {
        this.checkBox = [
            {
                id: 0,
                name: "SEG",
                value: "SEG",
                label: "Segunda",
                checked: false
            },
            {
                id: 1,
                name: "TER",
                value: "TER",
                label: "Terça",
                checked: false
            },
            {
                id: 2,
                name: "QUA",
                value: "QUA",
                label: "Quarta",
                checked: false
            },
            {
                id: 3,
                name: "QUI",
                value: "QUI",
                label: "Quinta",
                checked: false
            },
            { id: 4, name: "SEX", value: "SEX", label: "Sexta", checked: false }
        ];
    }

    ngOnInit() {
        this.getDetahesContrato();
        this.formulario(this.planoContratado);
        this.services = this.activedRoute.snapshot.data["findAllService"];
        this.configParametros =  this.activedRoute.snapshot.data["config"];
        this.calcularNumerosSessao();
    }
    ngAfterViewChecked(): void {
        this.cd.detectChanges();
    }

    /*Metodo que cria um fomulario e popupla um objeto do tipo planoContratado
   @param planoContratado PlanoContratado - varivael que contem um plano contratado para ser populado
   @return void*/
    formulario(planoContratado: FormPlanoContratado) {
        this.form = this.formBuilder.group({
            id: [],
            numeroContrato: [],
            nomePaciente: [],
            tipoContrato: [planoContratado.tipoContrato, [Validators.required]],
            servico: [planoContratado.servico, [Validators.required]],
            horarioEntrada: [planoContratado.horarioEntrada,[Validators.required]],
            sessao: ['', [Validators.required]],
            horarioSaida: [planoContratado.horarioSaida, [Validators.required]],
            diaConsulta: [planoContratado.diaConsulta],
            valorPlano: [planoContratado.valorPlano, [Validators.required]],
            valorTotal: [planoContratado.valorTotal]
        });
        this.mapDiaConsulta(planoContratado.diaConsulta);
        this.formOculto = [
            {
                label: "Tipo do plano:",
                value: this.planoContratado.tipoContrato
            },
            { label: "Serviço:", value: this.planoContratado.servico }
        ];
    }
    /*Metodo marca os checkbox conforme os valores vindo do plano contratado
    @params diaConsulta String[] - recebe os dias da consulta vindo do plano contratado*/
    mapDiaConsulta(diaConsulta: String[]) {
        if (diaConsulta != null) {
            diaConsulta.forEach(data => {
                this.checkBox.forEach(checkBox => {
                    if (data === checkBox.value) {
                        checkBox.checked = true;
                    }
                });
            });
            this.diasSemana = diaConsulta;
        }
    }
    /*Metodo que mapeia uma string para que seja compativel para inserir o valor no campo time do formulario
   @param data HorarioEntradaOrHorarioSaida - variavel que recebe os valores de horario de entrada e horario de horario de saida
   @return horarios String -  retorna o valor ja formatado*/
    mapHorarioSaidaOrEntrada(data: HorarioEntradaOrHorarioSaida) {
        let hour: String = new String(data.hour);
        let minute: String = new String(data.minute);
        let horarios: String =
            ("0" + hour).slice(-2) + ":" + ("0" + minute).slice(-2);
        return horarios;
    }
    calcularValorTotal() {
        let valorTotal =
            this.diasSemana.length * 4 * this.form.get("valorPlano").value * this.form.get("sessao").value;
        this.form.get("valorTotal").setValue(valorTotal);
    }
    valorSessao(event) {
        if (event) {
            this.services.forEach(data => {
                if (this.form.get("servico").value === data.servico) {
                    this.form.get("valorPlano").setValue(data.valor);
                }
            });
        }
    }
    /*Metodo calcula o numeros de sessao com base no horario de entrada e saida
    @return void*/
    calcularNumerosSessao(){
        let minutosSessao = this.configParametros.tempoSessao['minute'];
        let horarioSaida = moment(this.form.get('horarioSaida').value, 'HH:mm').toObject();
        let horarioEntrada = moment(this.form.get('horarioEntrada').value, 'HH:mm').toObject();
        let parseMinutosHorarioSaida = this.parseMinutos(horarioSaida);
        let parseMinutosHorarioEntrada = this.parseMinutos(horarioEntrada);
        let sessao = (parseMinutosHorarioSaida - parseMinutosHorarioEntrada) / minutosSessao;
        this.form.get('sessao').setValue(sessao);
    }
    /*Metodo transforma a hora em minutos 
    @param hora MomentObjectOutput - recebe uma hora formatada
    @return retorna as horas em minutos */ 
    parseMinutos(hora: moment.MomentObjectOutput){
        return (hora.hours * 60) + hora.minutes;
    }
    /*Metodo calcula o horario de saida com base no horario de entrada e o numero de sessoes
    @param event Blur - pega o event blur do campo sessao do formulario
    return void*/
    calcularTempoSessao(event: any) {
        if(event) {
            let sessao = this.form.get('sessao').value;
            let minutosSessao = sessao * this.configParametros.tempoSessao['minute'];
            let horaSaida = moment(this.form.get('horarioEntrada').value , 'HH:mm').add(minutosSessao, 'minutes').format('HH:mm');
            this.form.get('horarioSaida').setValue(horaSaida);
        }
    }
    /*Metodo que pega o nome do paciente e numero do contrato na pagina detalhes do contrato atraves de behavior subject
   @return void*/
    getDetahesContrato() {
        this.subscription = this.behaviorInformacoesContrato
            .getBehaviorView()
            .subscribe(data => {
                if (data != null) {
                    this.detalhesContrato = data;
                } else {
                    this.router.navigate([this.rota]);
                }
            });
    }

    /*Metodo que cancela a operacoes de novo servico e redireciona para pagina de detalhes do Contrato
   @return void*/
    cancelar() {
        this.form.reset();
        this.router.navigate(["contratos/" + this.detalhesContrato["0"]]);
    }

    /*Metodo que faz o submit do formulario e valida , depois redireciona para pagina detalhes do contrato
   @return void*/
    onSubmit() {
            if(this.diasSemana.length > 0){
            this.form.get("diaConsulta").setValue(this.diasSemana);
            if (this.form.valid) {
                this.form
                    .get("numeroContrato")
                    .setValue(this.detalhesContrato[0]);
                this.form.get("id").setValue(this.planoContratado.id);
                this.form
                    .get("nomePaciente")
                    .setValue(this.detalhesContrato[1]);
                this.event.emit(this.form.value);
            }
        }
        else {
            this.diasSemanaValidator = false;
        }
    }
    /*Metodo que captura os erros dos formControl, utilizado atraves do template
   @return void*/
    public hasError = (controlName: string, errorName: string) => {
        return this.form.controls[controlName].hasError(errorName);
    };


    /*Metodo que escusta o evento do checkbox e captura seus valores e colaca-os ne um array, quando um box e desmarcado
   seu valor e removido do array
   @param event  EventEmitter<MatCheckboxChange> - evento que pega as mudancas do checkbox
   return void*/
    changeCkeckBox(event: EventEmitter<MatCheckboxChange>) {
        if (this.diasSemana.indexOf(event.source["value"]) < 0) {
            this.diasSemana.push(event.source["value"]);
        } else {
            var index = this.diasSemana.indexOf(event.source["value"]);
            this.diasSemana.splice(index, 1);
        }
        if(this.diasSemana.length > 0){
            this.diasSemanaValidator = true;
        }
        this.calcularValorTotal();
    }
    ngOnDestroy(): void {
        this.subscription.unsubscribe();
    }
}
