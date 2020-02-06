import { ConfigParametro } from 'src/app/shared/model/config-parametros';
import { Subscription} from 'rxjs';
import { Router, ActivatedRoute } from '@angular/router';
import { ContratoService } from './../../../shared/Services/contrato.service';
import { Component, OnInit, OnDestroy} from '@angular/core';
import { GerarDescontoComponent } from './gerar-desconto/gerar-desconto.component'; 



import {
  ITdDataTableColumn, TdDataTableSortingOrder, ITdDataTableSortChangeEvent,
} from '@covalent/core/data-table';
import { IPageChangeEvent } from '@covalent/core/paging';
import { TdDialogService } from '@covalent/core/dialogs';
import { ImportComponent } from './import/import.component';
import { Contrato, PageContrato } from 'src/app/shared/model/Contrato';
import { BehaviorSubjectContratoRefreshService } from 'src/app/shared/Services/behavior-subject-contrato-refresh.service';
import { MatSelectChange } from '@angular/material/select';
import { PlanoContratadoService } from 'src/app/shared/Services/plano-contratado.service';
import { ConfigParametrosService } from 'src/app/shared/Services/config-parametros.service';

const DECIMAL_FORMAT: (v: any) => any = (v: number) => new Intl.NumberFormat('pt-BR',{style: 'currency', currency: 'BRL'} ).format(v);
const INT_FORMAT: (v: any) => any = (v: string) => parseInt(v);


@Component({
  selector: 'app-visualizar-contratos',
  templateUrl: './visualizar-contratos.component.html',
  styleUrls: ['./visualizar-contratos.component.css']
})
export class VisualizarContratosComponent implements OnInit, OnDestroy {


  columns: ITdDataTableColumn[] = [
    { name: 'numero', label: 'No. Contrato', format: INT_FORMAT, sortable: true  },
    { name: 'nomePaciente', label: 'Nome do Paciente', sortable: true},
    { name: 'tipoContratoTransient', label: 'Tipo do Contrato'},
    { name: 'ativo', label: 'Status do contrato', sortable: true},
    { name: 'valorTotal', label: 'Valor Contratado', numeric: true, format: DECIMAL_FORMAT, sortable: true},
    { name: 'diferenca', label: 'Saldo Mensal',numeric: true, format: DECIMAL_FORMAT}];
    
  statuContrato: any[] = [
    {value: null, viewValue: ''},
    {value: true, viewValue: 'Ativo'},
    {value: false, viewValue: 'Inativo'}
  ];

  pageContrato: PageContrato;
  subscription: Subscription;
  contratos: any[] = [];
  excludedColumnsFilterContrato: string[] = ['nomePaciente', 'valorTotal', 'id'];
  excludedColumnsFilterNomePaciente: string[] = ['valorTotal', 'numero', 'id'];
  total: number;
  configParametro: ConfigParametro;
  hoursByService: Map<String, String>;
  sortBy: string = '';
  statusContrato: boolean ;
  sortOrder: TdDataTableSortingOrder = TdDataTableSortingOrder.Ascending;
  page: number = 0;
  pageSize: number = 10;
  valorTotalContratosAtivos: any;
  numero: string = '';
  nomePaciente: string = '';
  numeroContrato: string = '';
  contrato: Contrato;
  findContratoAfterImport: Boolean = false;

  constructor(
    private _dialogService: TdDialogService,
    private contratoService: ContratoService,
    private configParametrosService: ConfigParametrosService,
    private activeRoute: ActivatedRoute,
    private behaviorRefreshTableContrato: BehaviorSubjectContratoRefreshService,
    private route: Router,
    private planoContratadoService: PlanoContratadoService  ) {}

  ngOnInit() {
    this.startTable();
    this.findServiceAndPlanoContratado();
    this.refreshTableContratoAfterImport();
    this.valorTotalContratosAtivos = this.activeRoute.snapshot.data[ 'contratosAtivos'];
  }
  findServiceAndPlanoContratado(){
      this.configParametrosService.findCofigParametros('1').subscribe(config => {
          this.configParametro = config;            
      });
      this.planoContratadoService.hoursByService().subscribe(data => {
        this.hoursByService = data;
        });

  }



  /*Metodo que projeta uma tabela inicial buscando os dados do banco atraves do contrato-resolver.resolve,
  pegando os dados do contrato-routing atraves de snapshot
  @return void*/
  startTable() {
    this.pageContrato = this.activeRoute.snapshot.data['contratos'];
    this.total = this.pageContrato.totalElements;
    this.contratos = this.pageContrato['content'];
  }
  /*Metodo que filtra os contratos atraves de determiandos parametros, trazendo os dados do banco e atribuindo a variavel
  contratos e assim atualizando o total de contratos e a primeira page do pagiable
  @return void*/
  findByFilter() {
    this.contratoService.findByFilters(this.nomePaciente, this.numero, this.page , this.pageSize, this.statusContrato, 'ASC', 'numero')
    .subscribe(pageFilter => {
      this.total = pageFilter.totalElements;
      this.contratos = pageFilter['content'];
    });
  }
  /*Metodo de filtro pelo status do Contrato
  @param event  MatSelectChange - evento de resgate de dados do mat-select proveniente do angular material
  @return void*/
  filterContratoStatusContrato(event: MatSelectChange) {
      this.statusContrato = event.value;
      this.findByFilter();
  }
  /*Metodo que preenche as informacoes do paginator
  @param event IPageChangeEvent - evento proveniente td-paging-bar vindo do teradata aonde recebe os parametros do paginator como
  pagesize e page
  @void*/
  changePageSize(event: IPageChangeEvent) {
    this.pageSize = event.pageSize;
    this.page = event.page - 1;
    this.findByFilter();
  }
  /*Metodo de filtro pelo campo numero do Contrato
  @param event - evento aonde recebe o texto digitado , aonde e disparado a requesicao apos um delay pre determinado
  @void*/
  filterContrato(event) {
      this.numero = event;
      this.findByFilter();
  }
   /*Metodo de filtro pelo campo numero nome do paciente
  @param event - evento aonde recebe o texto digitado , aonde e disparado a requesicao apos um delay pre determinado
  @void*/
  filterNomePaciente(event) {
      this.nomePaciente = event;
      this.findByFilter();
  }

  /*Metodo que ordena a coluna status do contrato atraves de um clique no header da coluna
  @param sortEvent ITdDataTableSortChangeEvent - evento vindo do teradata que captura o nome do campo e a direcao para ordenacao
  @return void*/
  sort(sortEvent: ITdDataTableSortChangeEvent): void {
    this.sortBy = sortEvent.name;
    this.sortOrder = sortEvent.order;
    this.contratoService.findAllContratos(this.page = 0, this.pageSize, this.sortOrder, this.sortBy).subscribe(data => {
      this.contratos = data['content'];
    });
  }


  /*Metodo que atualiza o data table apos a importacao for concluida com sucesso, ele consegue essa informacao atraves
  de um behavior object e assim atualizando o numero de contratos ativos
  @return void*/
  refreshTableContratoAfterImport() {
    this.subscription = this.behaviorRefreshTableContrato.getBehaviorView().subscribe(data => {
      if (data === true) {
        this.contratoService.findAllContratos(0 , 10, 'ASC' , 'id').subscribe( data => {
          this.total = data.totalElements;
          this.page = data.totalElements;
          this.contratos = data['content'];
        });
        this.planoContratadoService.findTotalActiveContracts().subscribe ( data => {
          this.valorTotalContratosAtivos = data;
        });
      }

    });
  }

  /*Metodo que abre o modal de import dos contratos
  @param event - recebe o evento onclick do botao de import
  @void*/
  openModalImport($event) {
    this._dialogService.open(ImportComponent, {
      width: '700px',
      height: ' 250px',
      disableClose: false
    });
  }

  /*Metodo que navega para o link contratos/detalhar/:id apos o usuario clicar em uma determinada linha da tabela, recuperando assim
  o numero do contrato que estava localizado na mesma
  @param event any - evento proveniente da td-data-table proveniente do teradata aonde que captura o clique nas linhas da table
  @return void*/
  rowClick(event: any){
    this.numeroContrato = event.row.numero;
    this.route.navigate(['contratos/' + this.numeroContrato ]);
  }




  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

}
