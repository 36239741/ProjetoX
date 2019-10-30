import { VisualizarContratosComponent } from './visualizar-contratos/visualizar-contratos.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ContratosComponent } from './contratos.component';
import { DetatalharContratosComponent } from './detalhar-contratos/detatalhar-contratos.component';





const routes: Routes = [
  {path: '', component: ContratosComponent,
  children: [
    {path: '', component: VisualizarContratosComponent, data: {breadcrumb: 'Visualizar Contratos'}},
    {path: 'detalhar/:id', component: DetatalharContratosComponent, data: {breadcrumb: 'Detalhar Contrato'}}
  ],
},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ContratosRoutingModule { }
