import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd, ActivatedRoute, PRIMARY_OUTLET } from '@angular/router';
import { BreadCrumb } from '../../model/breadcrumb';
import { filter, map } from 'rxjs/operators';
import { isNullOrUndefined } from 'util';

@Component({
  selector: 'app-bread-crumbs',
  templateUrl: './bread-crumbs.component.html',
  styleUrls: ['./bread-crumbs.component.css']
})
export class BreadCrumbsComponent implements OnInit {
  breadcrumbs:  BreadCrumb[] = [];
  url: String;
  label: String;
  notBreadcrumbs: Boolean = false;


  constructor(private router: Router,
              private  activatedRoute: ActivatedRoute) {
                this.createBreadCrumbs();
              }

  ngOnInit() {

  }
  createBreadCrumbs(){
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .pipe(map(() => this.activatedRoute))
      .pipe(map((route) => {
        while (route.firstChild) { route = route.firstChild; }
        return route;
      }))
      .pipe(filter(route => route.outlet === PRIMARY_OUTLET))
      .subscribe(route => {

        let snapshot = this.router.routerState.snapshot;
        this.url = snapshot.url;
        let routeData = route.snapshot.data;
        this.label = routeData['breadcrumb'];
        for(let bread of this.breadcrumbs) {
          if(bread.label === this.label){
            this.breadcrumbs.pop();
          }
        }
        this.breadcrumbs.push({
          url: this.url,
          label: this.label,
        });
      });

    }
}
