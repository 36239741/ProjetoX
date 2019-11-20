import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BehaviorInformacoesContratoService {

  constructor() { }

  private behave = new BehaviorSubject<String[]>(null);


setBehaviorView(behave: String[]) {
    this.behave.next(behave);
}
getBehaviorView(): Observable<String[]> {
    return this.behave.asObservable();
}
}
