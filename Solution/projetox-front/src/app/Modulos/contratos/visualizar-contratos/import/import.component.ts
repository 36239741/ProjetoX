import { TdLoadingService, LoadingType, LoadingMode } from '@covalent/core/loading';
import { ContratoService } from './../../../../shared/Services/contrato.service';
import { Component, OnInit } from '@angular/core';
import { TdDialogService } from '@covalent/core/dialogs';
import { FeedBack } from '../../../../shared/model/feedBack';
import { BehaviorMessageFeedBackService } from '../../../../shared/Services/behavior-message-feed-back.service';
import { BehaviorSubjectContratoRefreshService } from '../../../../shared/Services/behavior-subject-contrato-refresh.service';
import { ToastService } from 'src/app/shared/Services/toast.service';


@Component({
  selector: "app-import",
  templateUrl: './import.component.html',
  styleUrls: ['./import.component.css']
})
export class ImportComponent implements OnInit{

  constructor(private _dialogService: TdDialogService,
              private contratoService: ContratoService,
              private toast: ToastService,
              private loading: TdLoadingService,
              private behaviorSubject: BehaviorMessageFeedBackService,
              private behaviorRefreshTableContrato: BehaviorSubjectContratoRefreshService) {
                this.loading.create({
                  name: 'spinnerProgress',
                  type: LoadingType.Circular,
                  mode: LoadingMode.Indeterminate,
                  color: 'primary',
                });
              }

  ngOnInit() {}
  showSpinner:Boolean;
  isSuccess:Boolean = false;
  disabled: boolean = false;
  files: any;
  messageFeedBackOpen: boolean = false;
  messageFeedBack: FeedBack = new FeedBack();
  fileType:number = 0;
  formData : FormData = new FormData();
  habilityButton: Boolean = false;
  error: String = '';

  selectEvent(files: File): void {
    this.fileType = files.name.split('.').indexOf('xlsx') || files.name.split('.').indexOf('xls');
    if (files instanceof File && this.fileType > 0) {
      this.formData.append('file', files, files.name);
      this.habilityButton = true;
    } else {
      this.habilityButton = false;
      this.error = 'Verifique o arquivo a ser importado';
    }
  }

  import(){
    this.loading.register('spinnerProgress');
    this.contratoService.importContratos(this.formData).subscribe(res => {
      this.loading.resolve('spinnerProgress');
      this.messageFeedBackOpen = true;
      this.isSuccess = true;
      this.behaviorSubject.setBehaviorView(res);
      this.behaviorRefreshTableContrato.setBehaviorView(true);
    },
    (error: any) => {
      this.toast.toastError(error.error.message);
      this.loading.resolve('spinnerProgress')
    });

  }


  buttonFechar($event) {
    this._dialogService.closeAll();
  }


}
