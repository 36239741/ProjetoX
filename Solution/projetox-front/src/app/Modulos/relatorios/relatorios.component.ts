import { Component, OnInit } from '@angular/core';
import { ITdDataTableColumn, IPageChangeEvent } from '@covalent/core';
import {RelatorioService} from '../../shared/Services/relatorio.service';
import { saveAs } from 'file-saver';



const DECIMAL_FORMAT: (v: any) => any = (v: number) =>
    new Intl.NumberFormat("pt-BR", {
        style: "currency",
        currency: "BRL"
    }).format(v);
@Component({
  selector: 'app-relatorios',
  templateUrl: './relatorios.component.html',
  styleUrls: ['./relatorios.component.css']
})
export class RelatoriosComponent implements OnInit {
    
    columns: ITdDataTableColumn[] = [
        { name: "numeroContrato", label: "No. Contrato",},
        { name: "nomePaciente", label: "Nome do Paciente", },
        { name: "valorContratado", label: "Valor Contratado", numeric: true, format: DECIMAL_FORMAT },
        { name: "valorExecutado", label: "Valor Executado", numeric: true, format: DECIMAL_FORMAT },
        { name: "diferenca", label: "Diferença", numeric: true, format: DECIMAL_FORMAT }
    ];

    data: any = [];
    page: Number = 0;
    size: Number = 10;
    ano: Number = new Date().getFullYear();
    mes: Number = new Date().getMonth() + 1;
    total: Number = 0;
    verificadorLinha: any = 0;

  constructor(private relatorioService : RelatorioService) { }

  ngOnInit() {
    this.startTable();
  }

  startTable(){
    this.relatorioService.getRelatorios(this.page, this.size, this.ano, this.mes).subscribe(relatorio => {
        this.data = relatorio['pageList'];
        this.total = relatorio['nrOfElements'];
        this.data.forEach(element => {
          this.verificadorLinha = Math.sign(element['diferenca']);
          console.log(this.verificadorLinha);
          
        });
    });
  }


  chosenYearHandler(ano: any){
    this.ano = 0;
    this.ano = ano;
    console.log(this.ano , this.mes);

 }

 chosenMonthHandler(mes: any) {
    this.mes = 0;
    this.mes = mes + 1;
    console.log(this.ano , this.mes);
    this.startTable();

 }

 exportRelatorios() {
    this.relatorioService.exportPlanilhaRegistros(this.ano, this.mes).subscribe(planilhaRelatorio => {
        let date: Date = new Date();
        let file = new Blob([planilhaRelatorio], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });            
        var fileURL = URL.createObjectURL(file);
        saveAs(fileURL, 'Relatorios' + '_'+ this.mes + '-' + this.ano + '.xlsx');
    }, error => {
        console.log(error.error.message);
    });
}

changePageSize(event: IPageChangeEvent) {
    this.size = event.pageSize;
    this.page = event.page - 1;
    this.startTable();
  }

}
