import { ContratoService } from './../Services/contrato.service';
import { Injectable } from '@angular/core';

import { Resolve } from '@angular/router';
import { Observable } from 'rxjs';
import { PageContrato } from '../model/Contrato';


@Injectable({
  providedIn: 'root'
})
export class ContratoResolverResolve implements Resolve< Observable<PageContrato>>  {

  constructor(private contratoService: ContratoService) { }

  resolve(route: import("@angular/router").ActivatedRouteSnapshot, state: import("@angular/router").RouterStateSnapshot): Observable<PageContrato> | Observable<Observable<PageContrato>> | Promise<Observable<PageContrato>> {
    return this.contratoService.findAllContratos(0, 10, 'ASC', 'numero');
  }
}
