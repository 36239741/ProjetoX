import { Router, ActivatedRoute } from '@angular/router';
import { PlanoContratadoService } from './../../../shared/Services/plano-contratado.service';
import { FormPlanoContratado } from './../../../shared/model/formPlanoContratado';

import { Component, OnInit, ViewContainerRef} from '@angular/core';
import { TdDialogService } from '@covalent/core/dialogs';


@Component({
  selector: 'app-novo-servico',
  templateUrl: './novo-servico.component.html',
  styleUrls: ['./novo-servico.component.css']
})
export class NovoServicoComponent implements OnInit {
    rota:String ;
    planoContratado: FormPlanoContratado = new FormPlanoContratado();
    constructor(private planoService: PlanoContratadoService,
                private router: Router,
                private activedRoute: ActivatedRoute,
                private _dialogService: TdDialogService,
                private _viewContainerRef: ViewContainerRef){}
    ngOnInit(): void {
        this.activedRoute.params.subscribe(data =>{
            this.rota = '/contratos/' + data.id;
        });
    }
    recuperarForm(event: any) {
    let plano: any = event;
    this.planoService.savePlanoContratado(plano).subscribe(data => {
        this.router.navigate(['/contratos/', event.numeroContrato]);
        },
        err =>{
            this.openAlert(err.error.message);
        }
    );
    }

    openAlert(mensagemError: string): void {
        this._dialogService.openAlert({
          message: mensagemError,
          disableClose: false, // defaults to false
          viewContainerRef: this._viewContainerRef, //OPTIONAL
          title: 'Error', //OPTIONAL, hides if not provided
          closeButton: 'Close', //OPTIONAL, defaults to 'CLOSE'
          width: '400px', //OPTIONAL, defaults to 400px
        });
      }
}
