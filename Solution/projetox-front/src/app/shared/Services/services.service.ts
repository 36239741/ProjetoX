import { map, catchError } from 'rxjs/operators';
import { Servico } from './../model/Contrato';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { throwError, Observable } from 'rxjs';


const API_URL = 'http://localhost:4200/v1';

@Injectable({
  providedIn: 'root'
})
export class ServicesService {

  constructor(private http: HttpClient) { }
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

  findAll(): Observable<Servico[]>{
    return this.http.get<Servico[]>(API_URL + '/servicos').pipe(
      map((data: Servico[]) => data),
      catchError(this.handleError)
    );
  }
}
