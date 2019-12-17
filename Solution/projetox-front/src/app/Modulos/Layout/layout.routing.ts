import { LayoutComponent } from './layout.component';
import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { HomeComponent } from '../home';

const routes: Routes = [
{ path: '' , component: LayoutComponent ,
children: [
{ path: '', component: HomeComponent }]},



];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
  })
  export class LayoutRoutingModule { }
