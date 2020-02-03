import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Relatorio } from '../model/relatorio';


const API_URL = 'http://localhost:4200/v1';

@Injectable({
  providedIn: 'root'
})
export class RelatorioService {

constructor(private http: HttpClient) { }

getRelatorios(page:Number, size:Number, ano: Number, mes: Number): Observable<Relatorio> {
return this.http.get<Relatorio>(API_URL + '/relatorios?page=' + page + '&size=' + size + '&ano=' + ano + '&mes=' + mes);
}


exportPlanilhaRegistros(ano: Number, mes: Number): Observable<any>{
  const httpOptions = {
    'responseType'  : 'arraybuffer' as 'json'
  };
  return this.http.get<any>(API_URL + '/relatorios/export-relatorio?ano='+ ano + '&mes=' + mes, httpOptions);
}
}
