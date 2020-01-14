import { isNullOrUndefined } from 'util';
import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd, ActivatedRoute, PRIMARY_OUTLET } from '@angular/router';
import { BreadCrumb } from '../../model/breadcrumb';
import { filter, map } from 'rxjs/operators';

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
              }

  ngOnInit() {
    this.router.events
    .pipe(filter(event => event instanceof NavigationEnd))
    .subscribe(() => this.breadcrumbs = this.createBreadCrumbs(this.activatedRoute.root));

  }
  createBreadCrumbs(route: ActivatedRoute, url: string = '', breadCrumbs: BreadCrumb[] =[]): BreadCrumb[] {
    const children : ActivatedRoute[] = route.children;
    if(children.length === 0) {
      return breadCrumbs;
    }
    for (const child of children) {
      const routeUrl: string = child.snapshot.url.map(segment => segment.path).join('/');
      if(routeUrl != '') {
        url += `/${routeUrl}`;
      }

      const label = child.snapshot.data['breadCrumb'];
      if(!isNullOrUndefined(label)) {
        breadCrumbs.push({label, url});
      }
      console.log(url);
      return this.createBreadCrumbs(child, url , breadCrumbs);
    }
    return breadCrumbs;

}
}
