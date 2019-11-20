import { Injectable } from '@angular/core';

import { Resolve } from '@angular/router';
import { Observable } from 'rxjs';
import { PlanoContratadoService } from '../Services/plano-contratado.service';


@Injectable({
  providedIn: 'root'
})
export class findActiveContractNumberResolve implements Resolve< Observable<any>>  {

  constructor(private planoContratadoService: PlanoContratadoService) { }

  resolve(route: import("@angular/router").ActivatedRouteSnapshot, state: import("@angular/router").RouterStateSnapshot): Observable<any> | Observable<Observable<any>> | Promise<Observable<any>> {
    return this.planoContratadoService.findTotalActiveContracts();
  }
}
