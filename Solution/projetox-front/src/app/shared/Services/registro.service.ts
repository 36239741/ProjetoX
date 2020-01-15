import { Registro } from './../model/registro';
import { Observable } from 'rxjs';
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
  saveHoraSaida(numeroContrato: String): Observable<Registro>{
    return this.http.post<Registro>(API_URL + '/registros/save-saida', numeroContrato);
  }
  trocaServico(situacaoRegistro: String, registroId: Number, servico: String, valorSessao: Number):Observable<Registro>{
    return this.http.post<Registro>(API_URL + '/registros/trocar-servico?situacaoRegistro='+ situacaoRegistro + '&registroId=' +registroId
     + '&servico='+servico + '&valorSessao=' + valorSessao, null);
  }
  findAllRegistro(numeroContrato: String, page: Number, size: Number): Observable<Registro>{
    return this.http.get<Registro>(API_URL + '/registros/find-all?numeroContrato='+ numeroContrato + '&page=' + page + '&size=' + size);
  }
  findByDate(dataInicial:String, dataFinal:String, contratoId: String , page: Number, size: Number): Observable<Registro> {
    return this.http.get<Registro> (API_URL + '/registros/find-by-date?dataInicial='+ dataInicial + '&dataFinal=' + dataFinal + '&contratoId='
    + contratoId + '&page=' + page + '&size=' + size);
  }
}
