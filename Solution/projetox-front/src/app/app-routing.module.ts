import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { LoginComponent } from './Modulos/login/login/login.component';



const routes: Routes = [
  {path: '', component: LoginComponent},
  {path: 'home', loadChildren: () => import('./Modulos/Layout/layout.module').then(m => m.LayoutModule)},
  {path: 'contratos', loadChildren: () => import('../app/Modulos/contratos/contratos.module').then(m => m.ContratosModule)},
  {path: 'relatorios', loadChildren: () => import('../app/Modulos/relatorios/relatorios.module').then(m => m.RelatoriosModule)}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
