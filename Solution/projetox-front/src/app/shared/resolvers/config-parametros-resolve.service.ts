import { ConfigParametrosService } from './../Services/config-parametros.service';
import { Injectable } from '@angular/core';
import { ConfigParametro } from '../model/config-parametros';
import { Observable } from 'rxjs';
import { Resolve } from '@angular/router';


@Injectable({
  providedIn: 'root'
})
export class ConfigParametrosResolve implements Resolve<Observable<ConfigParametro>> {


  constructor(private configParametrosService: ConfigParametrosService) { }
  resolve(route: import("@angular/router").ActivatedRouteSnapshot, state: import("@angular/router").RouterStateSnapshot): Observable<ConfigParametro> | Observable<Observable<ConfigParametro>> | Promise<Observable<ConfigParametro>> {
    return this.configParametrosService.findCofigParametros('1');
  }
}
