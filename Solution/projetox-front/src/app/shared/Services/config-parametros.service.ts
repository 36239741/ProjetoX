import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ConfigParametro } from '../model/config-parametros';

const API_URL = 'http://localhost:4200/v1';
@Injectable({
  providedIn: 'root'
})
export class ConfigParametrosService {

  constructor(private http: HttpClient) { }

  findCofigParametros(configId: String): Observable<ConfigParametro> {
    return this.http.get<ConfigParametro>(API_URL + '/config-parametros/' + configId);
  }
}
