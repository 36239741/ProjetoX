import { Router, ActivatedRoute } from '@angular/router';
import { PlanoContratadoService } from './../../../shared/Services/plano-contratado.service';
import { FormPlanoContratado } from './../../../shared/model/formPlanoContratado';

import { Component, OnInit} from '@angular/core';
import { ToastService } from 'src/app/shared/Services/toast.service';


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
                private toast: ToastService){}
    ngOnInit(): void {
        this.activedRoute.params.subscribe(data =>{
            this.rota = '/contratos/' + data.id;
        });
    }
    recuperarForm(event: any) {
    let plano: any = event;
    this.planoService.savePlanoContratado(plano).subscribe(data => {
        this.router.navigate(['/contratos/', event.numeroContrato]);
        this.toast.toastSuccess('Serviços criado com sucesso. ');
        },
        err =>{
            this.toast.toastError(err.error.message);
        }
    );
    }


}
