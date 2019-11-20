import { HorarioEntradaOrHorarioSaida, Contrato } from './../../../shared/model/Contrato';
import { Component, OnInit} from '@angular/core';
import { ITdDataTableColumn } from '@covalent/core/data-table';
import { ActivatedRoute } from '@angular/router';
import { BehaviorInformacoesContratoService } from 'src/app/shared/Services/behavior-informacoes-contrato.service';
import { BehaviorPlanoContratadoService } from 'src/app/shared/Services/behavior-plano-contratado.service';


const DECIMAL_FORMAT: (v: any) => any = (v: number) => new Intl.NumberFormat('pt-BR',{style: 'currency', currency:'BRL'} ).format(v);
const DATA_FORMAT: (v: any) => any = (v: HorarioEntradaOrHorarioSaida) => {return ('0' + v.hour).slice(-2) + ':' + ('0' + v.minute).slice(-2)};
const DIAS_FORMAT: (v: any) => any = (v: any) => {var diasSemana =
    v.map(function(v){return v['diasSemana'];
})
return diasSemana};

@Component({
  selector: 'app-detatalhar-contratos',
  templateUrl: './detatalhar-contratos.component.html',
  styleUrls: ['./detatalhar-contratos.component.css']
})
export class DetatalharContratosComponent implements OnInit {


  contrato: Contrato = new Contrato();
  data: any[] = [];
  numeroContrato: number = 0;
  totalPlanoParticular: number = 0;
  totalPLanoMensal: number = 0;
  constructor(private activeRoute: ActivatedRoute,
              private behaviorInformacoesContrato: BehaviorInformacoesContratoService,
              private behaviorPlanoContratado: BehaviorPlanoContratadoService) { }

  ngOnInit() {
    this.lodingTable();

  }

  columns: ITdDataTableColumn[] = [
  { name: 'tipoContrato', label: 'Tipo do Contrato'},
  { name: 'servico.servico', label: 'Serviço', width: {min:150} },
  { name: 'sessao', label: 'Sessões', },
  { name: 'horarioEntrada', label: 'Entrada Padrão', format: DATA_FORMAT},
  { name: 'horarioSaida', label: 'Saída Padrão', format: DATA_FORMAT},
  { name: 'diaConsulta', label: 'Dias da Semana',width: {min: 300} ,format: DIAS_FORMAT},
  { name: 'valorPlano',label: 'Valor do Plano',numeric: true, format: DECIMAL_FORMAT},
  { name: 'acoes', label: 'Ações'}
  ];

  lodingTable(): void{
    this.findContrato();
    this.pegarDetalhesContrato();
    console.log(this.contrato);

  }
  /*Metodo que recupera o contrato atraves de um snapshot na rota ativa do detalhar-contratos.module
  @return void*/
  findContrato(): void{
    this.contrato = this.activeRoute.snapshot.data['findByContrato'];
    this.data.push(this.contrato.planoContratado);
    this.calcularValorPlanos();
  }

  /*Metodo que calcula o valor total dos planos
  @return void*/
  calcularValorPlanos(){
     this.contrato.planoContratado.forEach(data => {
       if(data.tipoContrato.toLocaleLowerCase().trim() === 'plano'){
          this.totalPLanoMensal += data.valorTotal;
       }
       else{
         this.totalPlanoParticular += data.valorTotal;
       }
     });
  }

  editarPlano(event){
    this.behaviorPlanoContratado.setBehaviorView(event);
  }

  pegarDetalhesContrato(){
    let detalhesContrato: String[] = [];
    detalhesContrato.push(this.contrato.numero);
    detalhesContrato.push(this.contrato.nomePaciente);
    this.behaviorInformacoesContrato.setBehaviorView(detalhesContrato);
  }
}
