import { MatSnackBar } from "@angular/material";
import { ToastService } from "src/app/shared/Services/toast.service";
import {
    Contrato,
    HorarioEntradaOrHorarioSaida
} from "src/app/shared/model/Contrato";
import { ContratoService } from "./../../../shared/Services/contrato.service";
import { Component, OnInit, ViewContainerRef } from "@angular/core";
import { RegistroService } from "../../../shared/Services/registro.service";
import { SnackBarClockBiometriaService } from "../../../shared/Services/snack-bar-clock-biometria.service";
import {
    ITdDataTableColumn,
    ITdDataTableRowClickEvent
} from "@covalent/core/data-table";
import { TdDialogService } from "@covalent/core/dialogs";
import { CofigConfirmDialog } from 'src/app/shared/components/dialog-confirm/config-confirm-dialog';
import { DialogConfirmComponent } from 'src/app/shared/components/dialog-confirm/dialog-confirm.component';

const DATA_FORMAT: (v: any) => any = (v: HorarioEntradaOrHorarioSaida) => {
    return ("0" + v.hour).slice(-2) + ":" + ("0" + v.minute).slice(-2);
};
const DIAS_FORMAT: (v: any) => any = (v: any) => {
    var diasSemana = v.map(function(v) {
        return v["diasSemana"];
    });
    return diasSemana;
};
@Component({
    selector: "app-registro-biometria",
    templateUrl: "./registro-biometria.component.html",
    styleUrls: ["./registro-biometria.component.css"]
})
export class RegistroBiometriaComponent implements OnInit {
    columns: ITdDataTableColumn[] = [
        { name: "tipoContrato", label: "Tipo do Contrato" },
        { name: "servico.servico", label: "Serviço", width: { min: 200 } },
        { name: "sessao", label: "Sessões" },
        { name: "horarioEntrada",label: "Entrada Padrão",format: DATA_FORMAT},
        { name: "horarioSaida", label: "Saída Padrão", format: DATA_FORMAT },
        { name: "diaConsulta", label: "Dias da Semana", width: { min: 300 },format: DIAS_FORMAT}
    ];
    servico: any;
    data: any[] = [];
    contrato: Contrato;
    hideTable: boolean = false;
    noDataMessage: String = "Nenhum serviço encontrado";
    operacao: String = "";

    constructor(
        private contratoService: ContratoService,
        private snackBar: MatSnackBar,
        private toastService: ToastService,
        private clockBiometriaService: SnackBarClockBiometriaService,
        private registroService: RegistroService,
        private _dialogService: TdDialogService,
        private _viewContainerRef: ViewContainerRef
    ) {}

    ngOnInit() {}
    saveEntrada() {
        this.operacao = "entrada";
        this.findContratoByBiometria();
    }
    saveSaida() {
        this.operacao = "saida";
        this.findContratoByBiometria();
    }

    openClockBiometria() {
        this.clockBiometriaService.openSnackBarClockBiometria(
            "Posicione o dedo indicador sobre a superfície do leitor biométrico e retire após o bipe, o leitor ficará em modo de captura por 30 segundos."
        );
    }
    findContratoByBiometria() {
        this.contratoService.findByBiometria().subscribe(
            contrato => {
                this.snackBar.dismiss();
                this.contrato = contrato;
                this.data = [];
                contrato.planoContratado.forEach(plano => {
                    if (plano.ativo == true) {
                        this.data.push(plano);
                    }
                });
                if (this.operacao == "entrada") {
                    this.hideTable = true;
                } else {
                    this.registroService
                        .saveHoraSaida(this.contrato.numero)
                        .subscribe(
                            registro => {
                                this.toastService.toastSuccess(
                                    "Atendimento do paciente " +
                                        registro.contrato.nomePaciente +
                                        " encerrado com sucesso."
                                );
                            },
                            error => {
                                this.toastService.toastError(
                                    error.error.message
                                );
                            }
                        );
                }
            },
            error => {
                this.snackBar.dismiss();
                this.toastService.toastError(error.error.message);
            }
        );
    }
    rowClick(event: ITdDataTableRowClickEvent) {
        this.openConfirm(event);
    }
    openConfirm(event: ITdDataTableRowClickEvent): void {
        let configDialog: CofigConfirmDialog = {
            title: 'Confirmar registro de entrada do paciente',
            message:"Deseja confirmar o registro de entrada do paciente " +this.contrato.nomePaciente +" no atendimento para o serviço " + event.row.servico.servico + "?" ,
            acceptButton: 'Confirmar',
            cancelButton: 'Fechar'
        };
        this._dialogService
            .open(DialogConfirmComponent, {
            width: '700px',
            height: '250px',
            data: configDialog
            })
            .afterClosed()
            .subscribe((accept: boolean) => {
                if (accept) {
                    this.registroService
                        .saveHoraEntrada(this.contrato.numero, event.row.id)
                        .subscribe(
                            registro => {
                                this.toastService.toastSuccess("Registro de entrada do paciente "+ registro.contrato.nomePaciente +" realizado com sucesso.");
                            },
                            error => {
                                this.toastService.toastError(error.error.message);
                            }
                        );
                    this.hideTable = false;
                }
            });
    }
}
