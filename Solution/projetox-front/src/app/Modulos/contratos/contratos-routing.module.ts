import { VisualizarContratosComponent } from './visualizar-contratos/visualizar-contratos.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ContratosComponent } from './contratos.component';
import { DetatalharContratosComponent } from './detalhar-contratos/detatalhar-contratos.component';
import { ContratoResolverResolve } from '../../shared/resolvers/contrato-resolver.resolve';
import { ContratoResolveFindByNumeroResolve } from '../../shared/resolvers/contrato-resolve-find-by-numero';




const routes: Routes = [
  {path: '', component: ContratosComponent,
  children: [
    {path: '', component: VisualizarContratosComponent,
    data: {breadcrumb: 'Visualizar Contratos',},
    resolve: {contratos : ContratoResolverResolve}},

    {path: 'detalhar/:id', component: DetatalharContratosComponent,
    data: {breadcrumb: 'Detalhar Contrato'},
    resolve: {findByContrato: ContratoResolveFindByNumeroResolve}}
  ],
},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ContratosRoutingModule { }
