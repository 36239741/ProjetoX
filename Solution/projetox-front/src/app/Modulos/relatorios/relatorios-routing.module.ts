import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { RelatoriosComponent } from './relatorios.component';
import { LayoutComponent } from '../Layout/layout.component';






const routes: Routes = [
    {path: '', component: LayoutComponent,
    children: [ {path: '', component: RelatoriosComponent}]},
 

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RelatorioRoutingModule { }
