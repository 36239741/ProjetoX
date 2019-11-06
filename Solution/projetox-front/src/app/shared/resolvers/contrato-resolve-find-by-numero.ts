import { ContratoService } from './../Services/contrato.service';
import { Injectable } from '@angular/core';

import { Resolve } from '@angular/router';
import { Observable } from 'rxjs';
import { Contrato } from '../model/Contrato';

@Injectable({
  providedIn: 'root'
})
export class ContratoResolveFindByNumeroResolve implements Resolve<Observable<Contrato>> {


  constructor(private contratoService: ContratoService) { }

  resolve(route: import("@angular/router").ActivatedRouteSnapshot, state: import("@angular/router").RouterStateSnapshot): Observable<Contrato> | Observable<Observable<Contrato>> | Promise<Observable<Contrato>> {
    return this.contratoService.findByContrato(route.params.id);
  }
}
