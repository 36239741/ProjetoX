import { ContratoService } from '../Services/contrato.service';
import { Injectable } from '@angular/core';

import { Resolve } from '@angular/router';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class findActiveContractNumberResolve implements Resolve< Observable<Number>>  {

  constructor(private contratoService: ContratoService) { }

  resolve(route: import("@angular/router").ActivatedRouteSnapshot, state: import("@angular/router").RouterStateSnapshot): Observable<Number> | Observable<Observable<Number>> | Promise<Observable<Number>> {
    return this.contratoService.findActiveContractNumber();
  }
}
