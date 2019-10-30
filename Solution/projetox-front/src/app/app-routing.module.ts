import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './Modulos/home/home.component';
import { LoginComponent } from './Modulos/login/login/login.component';



const routes: Routes = [
  {path: '', component: LoginComponent},
  {path: 'home', component: HomeComponent},
  {path: 'contratos', loadChildren: () => import('./Modulos/contratos/contratos.module').then(m => m.ContratosModule),
  data: {breadcrumb: 'Contratos'}},
  {path: 'relatorios', loadChildren: () => import('./Modulos/relatorios/relatorios.module').then(m => m.RelatoriosModule)}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
