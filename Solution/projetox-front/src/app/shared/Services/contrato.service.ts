import { Injectable } from '@angular/core';

import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { map, catchError } from 'rxjs/operators';
import { Observable, throwError } from 'rxjs';
import { PageContrato } from '../model/page-contrato';
import { Contrato } from '../model/Contrato';
import { FeedBack } from '../model/feedBack';


const API_URL = 'http://localhost:8080';

@Injectable({
  providedIn: 'root'
})
export class ContratoService {

  constructor(private httpClient : HttpClient) { }

    // Handle API errors
    handleError(error: HttpErrorResponse) {
      if (error.error instanceof ErrorEvent) {
        // A client-side or network error occurred. Handle it accordingly.
        console.error('An error occurred:', error.error.message);
      } else {
        // The backend returned an unsuccessful response code.
        // The response body may contain clues as to what went wrong,
        console.error(
          `Backend returned code ${error.status}, ` +
          `body was: ${error.error}`);
      }
      // return an observable with a user-facing error message
      return throwError(
        'Something bad happened; please try again later.');
    };

  findAllContratos(page:number , size:number ): Observable<PageContrato>{
    return this.httpClient.get<PageContrato>(API_URL + '/contratos?page='+ page + "&size=" + size).pipe(
     map((data: PageContrato) => data),
     catchError(this.handleError)

   );
  }
  findByContrato(numero: number): Observable<Contrato>{
    return this.httpClient.get<Contrato> (API_URL + '/contratos/' + numero).pipe(
      map((data: Contrato) => data),
      catchError(this.handleError)
    );
  }
  findByFilters(nome: String, numero: String, page: number, size: number): Observable<PageContrato>{
    return this.httpClient.get<PageContrato>(API_URL + '/contratos/filter?nomePaciente=' + nome +
    '&numero=' + numero + '&page=' + page + '&size=' + size ).pipe(map((data: PageContrato) => data),
    catchError(this.handleError));
  }

  importContratos(xlsx: FormData): Observable<FeedBack>{
    return this.httpClient.post<FeedBack>(API_URL + '/contratos', xlsx);
  }

}
