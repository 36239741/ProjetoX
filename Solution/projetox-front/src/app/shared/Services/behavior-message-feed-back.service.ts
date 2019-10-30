import { FeedBack } from './../model/feedBack';
import { Injectable } from '@angular/core';

import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BehaviorMessageFeedBackService {

  constructor() { }

private behave = new BehaviorSubject<FeedBack>(new FeedBack());


setBehaviorView(behave: FeedBack) {
    this.behave.next(behave);
}
getBehaviorView(): Observable<FeedBack> {
    return this.behave.asObservable();
}
}
