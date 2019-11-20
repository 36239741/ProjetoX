import { PlanoContratado } from './../model/plano-contradao';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BehaviorPlanoContratadoService {

  constructor() { }


private behave = new BehaviorSubject<PlanoContratado>(new PlanoContratado());


setBehaviorView(behave: PlanoContratado) {
    this.behave.next(behave);
}
getBehaviorView(): Observable<PlanoContratado> {
    return this.behave.asObservable();
}
}
