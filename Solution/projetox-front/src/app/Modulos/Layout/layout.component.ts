import { Component, OnInit, Input } from '@angular/core';

import { MenuList } from './MenuList';
import { MatIconRegistry } from '@angular/material/icon';
import { DomSanitizer } from '@angular/platform-browser';
import { LogoutService } from '../../shared/Services/logout.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.css']
})
export class LayoutComponent implements OnInit {

  constructor(
    iconRegistry: MatIconRegistry,
    sanitizer: DomSanitizer,
    private logoutService: LogoutService,
    private route: Router
    )
    {
    iconRegistry.addSvgIcon('menu-icon', sanitizer.bypassSecurityTrustResourceUrl('assets/icons/menu-icon.svg'));
    iconRegistry.addSvgIcon('contratos-icon', sanitizer.bypassSecurityTrustResourceUrl('assets/icons/contratos.svg'));
    iconRegistry.addSvgIcon('relatorios-icon', sanitizer.bypassSecurityTrustResourceUrl('assets/icons/relatorios.svg'));

  }

  listMenu: MenuList[] = [{
    menuIcon: 'contratos-icon',
    menuName: 'Contratos',
    link: '/contratos'
  },
{
  menuIcon: 'relatorios-icon',
  menuName: 'Relatorios',
  link: '/relatorios'
}];

  @Input() title: string = '';


  ngOnInit() {

  }

  logout(){
    this.logoutService.logout();
    this.route.navigate(['/']);
  }
}
