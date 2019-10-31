import { Subscription } from 'rxjs';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { BehaviorMessageFeedBackService } from 'src/app/shared/Services/behavior-message-feed-back.service';


@Component({
  selector: 'app-import-feed-back',
  templateUrl: './import-feed-back.component.html',
  styleUrls: ['./import-feed-back.component.css']
})
export class ImportFeedBackComponent implements OnInit, OnDestroy {


  message: String = '';
  subscription: Subscription;
  constructor(private behaviorFeedBack: BehaviorMessageFeedBackService) { }

  ngOnInit() {
    this.subscription = this.behaviorFeedBack.getBehaviorView().subscribe((message) =>  this.message = ' Foram salvos: '
    + message.save + ' Contratos' + ' e ' +
     'Foram atualizados: ' + message.update + ' Contratos');
  }


  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

}
