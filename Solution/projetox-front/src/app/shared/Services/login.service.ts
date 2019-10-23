import { Injectable } from '@angular/core';

import { HttpClient, HttpHeaders } from '@angular/common/http';
import { map } from 'rxjs/operators';

const API_URL = 'http://localhost:8080';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(
    private http: HttpClient ) { }


  autenticacaoLogin(email: string, senha: string){
    const headers = new HttpHeaders({ Authorization: 'Basic ' + btoa(email + ':' + senha)});

    return this.http.get(API_URL + '/login', {headers}).pipe(
      map(
        userData => {
          sessionStorage.setItem('email', email);
          return userData;
        }
      )
    );

    }
    isUserLoggedIn() {
      let user = sessionStorage.getItem('email');
      return !(user === null)
    }

}
