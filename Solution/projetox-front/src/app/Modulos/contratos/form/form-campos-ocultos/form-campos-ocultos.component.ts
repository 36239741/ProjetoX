import { Component, OnInit, Input } from '@angular/core';
import { FormOculto } from './form-oculto';

@Component({
  selector: 'app-form-campos-ocultos',
  templateUrl: './form-campos-ocultos.component.html',
  styleUrls: ['./form-campos-ocultos.component.css']
})
export class FormCamposOcultosComponent implements OnInit {
  @Input() formCampos: FormOculto[];
  constructor() { }

  ngOnInit() {
  }

}
