import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';
import { JwtHelperService } from '@auth0/angular-jwt';

const API_URL = 'http://localhost:8080';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(
    private http: HttpClient,
    private jwtHelper: JwtHelperService
    ) { }

  autenticacaoLogin(email: string, senha: string){
    console.log(email + senha);
    return this.http.post<{token: string, tipo: string}>(API_URL + '/authentication', {email,senha}).pipe(tap(res => {
      localStorage.setItem('token', res.tipo + res.token);
    }));
    }

  isAuthenticated(): boolean{
    const token = localStorage.getItem('token');
    return !this.jwtHelper.isTokenExpired(token);
  }

}
