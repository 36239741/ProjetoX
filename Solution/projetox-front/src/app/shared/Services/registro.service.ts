import { Registro } from './../model/registro';
import { Observable } from 'rxjs';
import { Contrato } from './../model/Contrato';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

const API_URL = 'http://localhost:4200/v1';
@Injectable({
  providedIn: 'root'
})
export class RegistroService {

  constructor(private http: HttpClient) { }

  saveHoraEntrada(numeroContrato: String, idPlanoContratado: String): Observable<any> {
    return this.http.post(API_URL + '/registros/save-entrada', {numeroContrato:numeroContrato, idPlanoContratado:idPlanoContratado});
  }
  saveHoraSaida(numeroContrato: String){
    this.http.post('/registros/save-saida', numeroContrato);
  }
  findAllRegistro(numeroContrato: String, page: Number, size: Number): Observable<Registro>{
    return this.http.get<Registro>(API_URL + '/registros/find-all?numeroContrato='+ numeroContrato + '&page=' + page + '&size=' + size);
  }
}
