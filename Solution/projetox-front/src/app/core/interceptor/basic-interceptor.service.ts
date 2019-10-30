import { Injectable } from '@angular/core';

import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';


@Injectable()
export class BasicInterceptorService implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if(sessionStorage.getItem('currentUser') != null){
      const basicHeader = req.clone({
        headers:  req.headers.set('Authorization', 'Basic ' + sessionStorage.getItem('currentUser'))
        });
      return next.handle(basicHeader);
    }
    return next.handle(req.clone());

    }


  constructor() {}
}
