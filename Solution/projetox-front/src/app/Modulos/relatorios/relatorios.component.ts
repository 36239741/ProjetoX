import { ContratoService } from './../../shared/Services/contrato.service';
import { Component, OnInit } from '@angular/core';
import { IPageChangeEvent } from '@covalent/core';
import { saveAs } from 'file-saver';
import { TableMaterialConfig } from 'src/app/shared/components/table-material/table-material-config';



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
    
    columns: TableMaterialConfig[] = [
        { name: "numero", label: "No. Contrato"},
        { name: "nomePaciente", label: "Nome do Paciente"},
        { name: "valorTotal", label: "Valor Contratado", format: DECIMAL_FORMAT},
        { name: "valorExecutado", label: "Valor Executado"},
        { name: "diferenca", label: "Diferença"}
    ];

    data: any = [];
    page: Number = 0;
    size: Number = 10;
    ano: Number = new Date().getFullYear();
    mes: Number = new Date().getMonth() + 1;
    total: Number = 0;
    verificadorLinha: any = 0;

  constructor(private contratoService : ContratoService) { }

  ngOnInit() {
    this.startTable();
    
  }

  startTable(){
    this.contratoService.getRelatorios(this.page, this.size, this.ano, this.mes).subscribe(contrato => {
        this.data = contrato['pageList'];
        this.total = contrato['nrOfElements'];
    });
  }

  decimalConverter(valor: any) {
   return new Intl.NumberFormat("pt-BR", {
        style: "currency",
        currency: "BRL"
    }).format(valor);
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
    this.contratoService.exportPlanilhaRegistros(this.ano, this.mes).subscribe(planilhaRelatorio => {
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
