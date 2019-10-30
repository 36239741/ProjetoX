import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd, ActivatedRoute, PRIMARY_OUTLET } from '@angular/router';
import { BreadCrumb } from '../../model/breadcrumb';
import { filter, distinctUntilChanged } from 'rxjs/operators';

@Component({
  selector: 'app-bread-crumbs',
  templateUrl: './bread-crumbs.component.html',
  styleUrls: ['./bread-crumbs.component.css']
})
export class BreadCrumbsComponent implements OnInit {
  breadcrumbs:  BreadCrumb[] = [];

  constructor(private router: Router,
              private  activateRoute: ActivatedRoute) {
                this.breadcrumbs = this.buildBreadCrumb(this.activateRoute.root);
              }

  ngOnInit() {
    this.router.events.pipe(
      filter((event) => event instanceof NavigationEnd),
      distinctUntilChanged(),
  ).subscribe(() => {
      this.breadcrumbs = this.buildBreadCrumb(this.activateRoute.root);

  });
  console.log(this.breadcrumbs);
  }

  buildBreadCrumb(route: ActivatedRoute, url: string = '', breadcrumbs: BreadCrumb[] = []): BreadCrumb[] {
  let label = route.routeConfig && route.routeConfig.data? route.routeConfig.data.breadcrumb : "";
  let path = route.routeConfig && route.routeConfig.data ? route.routeConfig.path : "";
  const lastRoutePart = path.split("/").pop();
  const isDynamicRoute = lastRoutePart.startsWith(":");
  if (isDynamicRoute && !!route.snapshot) {
  const paramName = lastRoutePart.split(":")[1];
  path = path.replace(lastRoutePart, route.snapshot.params[paramName]);
  label = route.snapshot.params[paramName];
  const nextUrl = path ? `${url}/${path}` : url;

  const breadcrumb: BreadCrumb = {
      label: label,
      url: nextUrl,
  };

  const newBreadcrumbs = breadcrumb.label ? [ ...breadcrumbs, breadcrumb ] : [ ...breadcrumbs];
  if (route.firstChild) {

      return this.buildBreadCrumb(route.firstChild, nextUrl, newBreadcrumbs);
  }
  return newBreadcrumbs;
}
  }
}
