import { SnackBarClockBiometriaService } from "./../../../shared/Services/snack-bar-clock-biometria.service";
import { ContratoService } from "./../../../shared/Services/contrato.service";
import { PlanoContratadoService } from "./../../../shared/Services/plano-contratado.service";
import {
    HorarioEntradaOrHorarioSaida,
    Contrato
} from "./../../../shared/model/Contrato";
import { Component, OnInit, ViewContainerRef } from "@angular/core";
import { ITdDataTableColumn } from "@covalent/core/data-table";
import { ActivatedRoute } from "@angular/router";
import { BehaviorInformacoesContratoService } from "src/app/shared/Services/behavior-informacoes-contrato.service";
import { BehaviorPlanoContratadoService } from "src/app/shared/Services/behavior-plano-contratado.service";
import { ToastService } from "src/app/shared/Services/toast.service";
import { MatSnackBar, MatDialogConfig, MatDialog } from "@angular/material";
import { GerarDescontoComponent } from "../visualizar-contratos/gerar-desconto/gerar-desconto.component";
import { DialogConfirmComponent } from 'src/app/shared/components/dialog-confirm/dialog-confirm.component';
import { CofigConfirmDialog } from 'src/app/shared/components/dialog-confirm/config-confirm-dialog';

const DECIMAL_FORMAT: (v: any) => any = (v: number) =>
    new Intl.NumberFormat("pt-BR", {
        style: "currency",
        currency: "BRL"
    }).format(v);
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
    selector: "app-detatalhar-contratos",
    templateUrl: "./detatalhar-contratos.component.html",
    styleUrls: ["./detatalhar-contratos.component.css"]
})
export class DetatalharContratosComponent implements OnInit {
    contrato: Contrato = new Contrato();
    data: any[] = [];
    situacaoBiometria: String;
    buttonBiometria: String;
    biometriaClock: number = 30;
    numeroContrato: number = 0;
    totalPlanoParticular: number = 0;
    totalPLanoMensal: number = 0;
    saldoTotal: number = 0;
    constructor(
        private activeRoute: ActivatedRoute,
        private dialogService: MatDialog,
        private _viewContainerRef: ViewContainerRef,
        private toastService: ToastService,
        private behaviorInformacoesContrato: BehaviorInformacoesContratoService,
        private behaviorPlanoContratado: BehaviorPlanoContratadoService,
        private planoContratadoService: PlanoContratadoService,
        private contratoService: ContratoService,
        private snackBarService: SnackBarClockBiometriaService,
        private snackBar: MatSnackBar
    ) {}

    ngOnInit() {
        this.lodingTable();
        this.situacaoDaBiometria();
    }

    columns: ITdDataTableColumn[] = [
        { name: "tipoContrato", label: "Tipo do Contrato" },
        { name: "servico.servico", label: "Serviço", width: { min: 200 } },
        { name: "sessao", label: "Sessões" },
        {
            name: "horarioEntrada",
            label: "Entrada Padrão",
            format: DATA_FORMAT
        },
        { name: "horarioSaida", label: "Saída Padrão", format: DATA_FORMAT },
        {
            name: "diaConsulta",
            label: "Dias da Semana",
            width: { min: 300 },
            format: DIAS_FORMAT
        },
        {
            name: "valorSessao",
            label: "Valor da sessão",
            numeric: true,
            format: DECIMAL_FORMAT
        },
        {
            name: "saldoMensal",
            label: "Saldo Mensal",
            numeric: true,
            format: DECIMAL_FORMAT
        },
        {
            name: "valorTotal",
            label: "Valor Total",
            numeric: true,
            format: DECIMAL_FORMAT
        },
        { name: "acoes", label: "Ações" }
    ];

    lodingTable(): void {
        this.findContrato();
        this.pegarDetalhesContrato();
    }

    findContratoService() {
        this.planoContratadoService
            .findAllPlanoContratado(this.activeRoute.snapshot.params.id)
            .subscribe(planoContratado => {
                this.data = [];
                this.data.push(planoContratado);
            });

        this.contratoService
            .findByContrato(this.activeRoute.snapshot.params.id)
            .subscribe(contrato => {
                this.contrato = contrato;
                this.calcularValorPlanos();
            });
    }

    /*Metodo que recupera o contrato atraves de um snapshot na rota ativa do detalhar-contratos.module
  @return void*/
    findContrato(): void {
        this.contrato = this.activeRoute.snapshot.data["findByContrato"];
        this.contrato.planoContratado = this.activeRoute.snapshot.data[
            "planoContratado"
        ];
        this.contrato.planoContratado.forEach(plano => {
            this.saldoTotal += plano.saldoMensal;
        });
        this.data.push(this.contrato.planoContratado);
        this.calcularValorPlanos();
    }
    openSnackBarClockBiometria() {
        this.snackBarService.openSnackBarClockBiometria(
            "Posicione o dedo indicador sobre a superfície do leitor biométrico e retire após o bipe. Repita o procedimento por três vezes,o leitor ficará em modo de captura por 30 segundos."
        );
    }
    saveBiometria() {
        this.contratoService.saveBiometria(this.contrato.numero).subscribe(
            () => {
                this.snackBar.dismiss();
                this.toastService.toastSuccess(
                    "Biometria cadastrada com sucesso."
                );
                this.contratoService
                    .findByContrato(this.activeRoute.snapshot.params.id)
                    .subscribe(contrato => {
                        this.contrato = contrato;
                        this.situacaoDaBiometria();
                    });
            },
            error => {
                this.snackBar.dismiss();
                this.toastService.toastError(error.error.message);
            }
        );
    }

    /*Metodo que calcula o valor total dos planos
  @return void*/
    calcularValorPlanos() {
        this.contrato.planoContratado.forEach(data => {
            if (data.tipoContrato.toLocaleLowerCase().trim() === "plano") {
                this.totalPLanoMensal += data.valorTotal;
            } else {
                this.totalPlanoParticular += data.valorTotal;
            }
        });
    }
    situacaoDaBiometria() {
        if (this.contrato.biometria == null) {
            this.situacaoBiometria = "Cadastrar";
            this.buttonBiometria = "Cadastrar Biometria";
            console.log(this.buttonBiometria);
        } else {
            this.situacaoBiometria = "Cadastrado";
            this.buttonBiometria = "Atualizar Biometria";
        }
    }

    editarPlano(event) {
        this.behaviorPlanoContratado.setBehaviorView(event);
    }
    /*Metodo que faz o delete logico de um plano contratado e atualiza os dados da tabela
  @param event any - recebe um evento que contem os dados do plano contratado da linha da tabela
  return void*/
    deletarPlano(event) {
        let configDialog: CofigConfirmDialog = {
            title: 'Confirma a exclusão do serviço',
            message: 'Deseja realmente excluir esse serviço?',
            acceptButton: 'Confirmar',
            cancelButton: 'Fechar'
        };

        this.dialogService
            .open(DialogConfirmComponent, {
                width: '700px',
                height: '250px',
                data: configDialog
            })
            .afterClosed()
            .subscribe((accept: boolean) => {
                if (accept) {
                    this.planoContratadoService
                        .deletePlanoContratado(event.id)
                        .subscribe(
                            data => {
                                this.planoContratadoService
                                    .findAllPlanoContratado(
                                        this.contrato.numero
                                    )
                                    .subscribe(data => {
                                        this.data = [];
                                        this.data.push(data);
                                        this.contrato.planoContratado = data;
                                        this.totalPLanoMensal = 0;
                                        this.totalPlanoParticular = 0;
                                        this.calcularValorPlanos();
                                        this.toastService.toastSuccess(
                                            "Serviço deletado com sucesso."
                                        );
                                    });
                                this.contratoService
                                    .findByContrato(
                                        parseInt(this.contrato.numero)
                                    )
                                    .subscribe(data => {
                                        this.contrato = data;
                                    });
                            },
                            error => {
                                this.toastService.toastError(
                                    error.error.message
                                );
                            }
                        );
                } else {
                    this.dialogService.closeAll();
                }
            });
    }

    /*Metodo que pega o numero do contrato e o nome do paciente e insere em um objeto behavior
  return void*/
    pegarDetalhesContrato() {
        let detalhesContrato: String[] = [];
        detalhesContrato.push(this.contrato.numero);
        detalhesContrato.push(this.contrato.nomePaciente);
        this.behaviorInformacoesContrato.setBehaviorView(detalhesContrato);
    }

    gerarDesconto() {
        let dialogConfig: MatDialogConfig<any> = {
            width: "700px",
            height: "250px",
            data: this.contrato
        };
        this.dialogService
            .open(GerarDescontoComponent, dialogConfig)
            .afterClosed()
            .subscribe(() => {
                this.findContratoService();
            });
    }
}
