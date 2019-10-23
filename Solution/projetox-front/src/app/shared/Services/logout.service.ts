import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LogoutService {

  constructor() { }

  logout(): void{
    sessionStorage.removeItem('email');
  }
}
