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
  trocaServico(registroId: Number, valorSessao: String):Observable<Registro>{
    return this.http.get<Registro>(API_URL + '/registros/trocar-servico?registroId=' +registroId + '&valorSessao=' + valorSessao);
  }
  ausenciaProfissional(registroId: Number):Observable<Registro>{
    return this.http.get<Registro>(API_URL + '/registros/ausencia-profissional?registroId=' +registroId);
  }
  findAllRegistro(numeroContrato: String, page: Number, size: Number): Observable<Registro>{
    return this.http.get<Registro>(API_URL + '/registros/find-all?numeroContrato='+ numeroContrato + '&page=' + page + '&size=' + size);
  }
  findByDate(dataInicial:String, dataFinal:String, numeroContrato: String , page: Number, size: Number): Observable<Registro> {
    return this.http.get<Registro> (API_URL + '/registros/find-by-date?dataInicial='+ dataInicial + '&dataFinal=' + dataFinal + '&numeroContrato='
    + numeroContrato + '&page=' + page + '&size=' + size);
  }
  exportPlanilhaRegistros(numeroContrato: String): Observable<any>{
    const httpOptions = {
      'responseType'  : 'arraybuffer' as 'json'
    };
    return this.http.get<any>(API_URL + '/registros/export-registro?numeroContrato='+numeroContrato, httpOptions);
  }
}
