import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { PlanoContratado } from '../model/plano-contradao';


const API_URL = 'http://localhost:8080';
@Injectable({
  providedIn: 'root'
})
export class PlanoContratadoService {

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

      findTotalActiveContracts(): Observable<any>{
      return this.httpClient.get<any>(API_URL + '/planos/valor-total').pipe(
        map((data : any) => data),
        catchError(this.handleError)
      );
    }
      savePlanoContratado(planoContratado: any): Observable<any> {
       return this.httpClient.post(API_URL + '/planos', planoContratado);
      }
      findPlanoContratados(numeroContrato: String, servico: String, tipoContrato: String): Observable<PlanoContratado>{
        return this.httpClient.get<PlanoContratado>(API_URL + '/find-plano-contratado?numero-contrato=' + numeroContrato +
        '&servico=' + servico + '&tipo-contrato=' + tipoContrato );
      }
      updatePlanoContratado(planoContratado: any): Observable<any> {
        return this.httpClient.put<PlanoContratado>(API_URL + '/planos', planoContratado);
      }
}
