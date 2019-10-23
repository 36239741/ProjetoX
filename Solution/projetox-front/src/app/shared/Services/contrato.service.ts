import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';
import { Contrato } from './../model/Contrato';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';


const API_URL = 'http://localhost:8080';

@Injectable({
  providedIn: 'root'
})
export class ContratoService {

  constructor(private httpClient : HttpClient) { }

  findAllContratos(page:number , size:number ): Observable<Contrato>{
   return this.httpClient.get<Contrato>(API_URL + '/contratos?page='+ page + "&size=" + size).pipe(map((data: Contrato) => data));
  }

}
