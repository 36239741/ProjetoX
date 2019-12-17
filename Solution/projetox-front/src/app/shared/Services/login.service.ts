import { Injectable } from '@angular/core';

import { HttpClient, HttpHeaders} from '@angular/common/http';
import { map } from 'rxjs/operators';


const API_URL = 'http://localhost:4200/v1';
@Injectable({
  providedIn: 'root'
})
export class LoginService {


  constructor(
    private http: HttpClient) {

    }

  autenticacaoLogin(email: string, senha: string){
    const basicEncoder =  btoa(email + ':' + senha);
    const headers = new HttpHeaders({ Authorization: 'Basic ' + basicEncoder});

    return this.http.get(API_URL + '/login', {headers}).pipe(
      map(
        userData => {
          sessionStorage.setItem('username', email);
          sessionStorage.setItem('currentUser',basicEncoder );
          return userData;
        }
      )
    );

    }
    isUserLoggedIn() {
      let user = sessionStorage.getItem('username');
      return !(user === null)
    }

}
