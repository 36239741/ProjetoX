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

  }

  listMenu: MenuList[] = [
    {
        menuIcon: 'home',
        menuName: 'Página incial',
        link: '/home',
        toolTip: 'Voltar a página inicial.'
      },
    {
    menuIcon: 'description',
    menuName: 'Contratos',
    link: '/contratos',
    toolTip: 'Visualizar contratos cadastrados.'
  },
{
  menuIcon: 'assessment',
  menuName: 'Relatorios',
  link: '/relatorios',
  toolTip: 'Visualizar relatorios'
}];

  @Input() title: string = 'Espaço especializado Gene Gouveia';


  ngOnInit() {

  }

  logout(){
    this.logoutService.logout();
    this.route.navigate(['/']);
  }
}
