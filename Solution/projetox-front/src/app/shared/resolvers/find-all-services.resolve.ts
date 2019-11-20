import { Servico } from './../model/Contrato';
import { Observable } from 'rxjs';
import { Resolve } from '@angular/router';
import { Injectable } from '@angular/core';
import { ServicesService } from '../Services/services.service';

@Injectable({
  providedIn: 'root'
})
export class FindAllResolve implements Resolve<Observable<Servico>>{


  constructor(private seervicosService: ServicesService) { }

  resolve(route: import("@angular/router").ActivatedRouteSnapshot, state: import("@angular/router").RouterStateSnapshot): Observable<Servico> | Observable<Observable<Servico>> | Promise<Observable<Servico>> {
    return this.seervicosService.findAll();
  }
}
