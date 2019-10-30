import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BehaviorSubjectContratoRefreshService {

  constructor() { }

  private behave = new BehaviorSubject<Boolean>(false);


setBehaviorView(behave: Boolean) {
    this.behave.next(behave);
}
getBehaviorView(): Observable<Boolean> {
    return this.behave.asObservable();
}
}
