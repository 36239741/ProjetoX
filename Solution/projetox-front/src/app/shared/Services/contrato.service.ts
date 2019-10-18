import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';


const API_URL = 'http://localhost:8080';

@Injectable({
  providedIn: 'root'
})
export class ContratoService {

  constructor(private httpClient : HttpClient) { }

  findAllContratos(){
   return this.httpClient.get(API_URL);
  }
}
