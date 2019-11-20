import { PlanoContratadoService } from './../Services/plano-contratado.service';
import { Injectable } from '@angular/core';

import { Resolve } from '@angular/router';
import { Observable } from 'rxjs';
import { Contrato } from '../model/Contrato';

@Injectable({
  providedIn: 'root'
})
export class FindAllPlanoContratadoResolve implements Resolve<Observable<Contrato>> {


  constructor(private planoContratadoService: PlanoContratadoService) { }

  resolve(route: import("@angular/router").ActivatedRouteSnapshot, state: import("@angular/router").RouterStateSnapshot): Observable<Contrato> | Observable<Observable<Contrato>> | Promise<Observable<Contrato>> {
    return this.planoContratadoService.findAllPlanoContratado(route.params.id);
  }
}
