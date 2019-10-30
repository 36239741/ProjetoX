import { Router, NavigationStart, NavigationEnd, NavigationCancel, NavigationError } from '@angular/router';
import { Component, OnInit, AfterViewChecked } from '@angular/core';

import 'hammerjs';
import { HttpInterceptor, HttpRequest, HttpHandler } from '@angular/common/http';
import { TdLoadingService, LoadingType, LoadingMode } from '@covalent/core/loading';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements HttpInterceptor, OnInit{


  intercept(req: HttpRequest<any>, next: HttpHandler) {
    const xhr = req.clone({
      headers: req.headers.set('X-Requested-With', 'XMLHttpRequest')
    });
    return next.handle(xhr);
  }


  constructor(private loading: TdLoadingService,
              private router: Router ){
                this.loading.create({
                  name: 'loading',
                  type: LoadingType.Circular,
                  mode: LoadingMode.Indeterminate,
                  color: 'primary'
                });
              }

  ngOnInit(): void {
    this.router.events
    .pipe(
      filter(
        event =>
        event instanceof NavigationStart ||
        event instanceof NavigationEnd ||
        event instanceof NavigationCancel ||
        event instanceof NavigationError,)
    ).subscribe(event => {
      if (event instanceof NavigationStart) {
        this.registerLoading();
        return;
      }
      else{
        this.resolverLoading();
      }

    });
    }

    registerLoading(): void{
      this.loading.register('loading');
    }
    resolverLoading(): void {
      this.loading.resolve('loading');
    }

  }
