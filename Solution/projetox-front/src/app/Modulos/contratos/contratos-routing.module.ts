import { LayoutComponent } from '../Layout/layout.component';
import { VisualizarContratosComponent } from './visualizar-contratos/visualizar-contratos.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DetatalharContratosComponent } from './detalhar-contratos/detatalhar-contratos.component';
import { ContratoResolverResolve } from '../../shared/resolvers/contrato-resolver.resolve';
import { ContratoResolveFindByNumeroResolve } from '../../shared/resolvers/contrato-resolve-find-by-numero';
import { findActiveContractNumberResolve } from '../../shared/resolvers/find-active-contract-number.resolve';
import { NovoServicoComponent } from './novo-servico/novo-servico.component';
import { FindAllResolve } from '../../shared/resolvers/find-all-services.resolve';
import { FindAllPlanoContratadoResolve } from '../../shared/resolvers/find-all-plano-contratado';
import { EditarPlanoContratadoComponent } from './editar-plano-contratado/editar-plano-contratado.component';
import { ConfigParametrosResolve } from '../../shared/resolvers/config-parametros-resolve.service';
import { RegistrosComponent } from './registros/registros.component';




const routes: Routes = [
  
  {path: '', component: LayoutComponent,data: {breadCrumb: 'Visualizar Contratos'},
  children: [
    {path: '', component: VisualizarContratosComponent,
    data: {breadCrumb: null},
    resolve: {contratos : ContratoResolverResolve,
             contratosAtivos : findActiveContractNumberResolve},
            },
    {path: ':id/registros', component: RegistrosComponent, 
    data: {breadCrumb: 'Registros do Contrato'}},
    {path: ':id', component: DetatalharContratosComponent,
    data: {breadCrumb: 'Detalhar Contrato'},
    resolve: {findByContrato: ContratoResolveFindByNumeroResolve, 
              planoContratado: FindAllPlanoContratadoResolve}},

    {path: ':id/novo-servico', component: NovoServicoComponent,
    data: {breadCrumb: 'Novo Serviço'},
     resolve: { findAllService: FindAllResolve,
                config: ConfigParametrosResolve }},

     {path: ':id/editar-servico', component: EditarPlanoContratadoComponent,
     data: {breadCrumb: 'Editar Serviço'},
     resolve: { findAllService: FindAllResolve,
                config: ConfigParametrosResolve }},
  ],
},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ContratosRoutingModule { }
