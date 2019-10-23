import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';

import { ITdDynamicElementConfig, TdDynamicElement, TdDynamicFormsComponent } from '@covalent/dynamic-forms';
import { LoginService } from 'src/app/shared/Services/login.service';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy {
    errorLogin: string = '';

   elements: ITdDynamicElementConfig[] =[
    {
      name: 'email',
      label: 'Username',
      type: TdDynamicElement.Input,
      minLength: 4,
      maxLength: 50,
      required: true
    },
    {
      name: 'senha',
      label: 'Password',
      type: TdDynamicElement.Password,
      minLength: 4,
      maxLength: 12,
      required: true
    }];

  constructor(
    private autenticacaoLogin: LoginService,
    private route: Router,

   ) { }

   @ViewChild('form',{static: false})
    form: TdDynamicFormsComponent;
    subscription: Subscription;

  ngOnInit() {
  }

/*  Funcao ira fazer uma requisao post para o backend enviando email e senha e recebera o token de autenticacao
  em caso de erro ira imprimir na tela o erro
  @return void */
  login(): void{
   const API_URL = 'http://localhost:4200';
   const email: string = this.form.form.get('email').value;
   const senha: string = this.form.form.get('senha').value;
   if(this.form.valid){
    this.subscription = this.autenticacaoLogin.autenticacaoLogin(email,senha).subscribe(
       () =>{
      this.route.navigate(['/home']);
       },
        err => {
          console.log(err);
          this.form.refresh();
          if(err.status === 401){
          this.errorLogin = 'Usuario ou senha estao incorretos.';
         }
        });

   }
  }
  ngOnDestroy() {
    this.subscription.unsubscribe();
  }



}
