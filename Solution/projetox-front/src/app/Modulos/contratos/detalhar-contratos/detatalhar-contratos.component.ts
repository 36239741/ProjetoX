import { map } from 'rxjs/operators';
import { HorarioEntradaOrHorarioSaida, Contrato } from './../../../shared/model/Contrato';
import { ContratoService } from './../../../shared/Services/contrato.service';
import { Component, OnInit} from '@angular/core';
import { ITdDataTableColumn } from '@covalent/core/data-table';
import { ActivatedRoute } from '@angular/router';
import { DiaConsulta } from 'src/app/shared/model/dia-consulta';

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
  constructor(private contratoService: ContratoService,
              private activeRoute: ActivatedRoute ) { }

  ngOnInit() {
    this.lodingTable();
  }

  columns: ITdDataTableColumn[] = [
  { name: 'tipoContrato', label: 'Tipo do Contrato' },
  { name: 'servico.servico', label: 'Serviço' },
  { name: 'sessao', label: 'Sessões' },
  { name: 'horarioEntrada', label: 'Entrada Padrão', format: DATA_FORMAT},
  { name: 'horarioSaida', label: 'Saída Padrão', format: DATA_FORMAT },
  { name: 'diaConsulta', label: 'Dias da Semana', format: DIAS_FORMAT},
  { name: 'valorPlano',label: 'Valor do Plano',numeric: true, format: DECIMAL_FORMAT},
  { name: 'acoes', label: 'Ações'}
  ];

  lodingTable(): void{
    this.findContrato();

  }
  findContrato(): void{
    this.contrato = this.activeRoute.snapshot.data['findByContrato'];
    this.data.push(this.contrato.planoContratado);
    this.calcularValorPlanos();
  }

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
}
