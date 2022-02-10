import {Component, EventEmitter, Inject, InjectionToken, OnInit} from '@angular/core';

export class EditDrugInfo {
  constructor(public link: string, public linkEmitter: EventEmitter<string>) {
  }
}

export const EDIT_DRUG_TOKEN = new InjectionToken('edit');

@Component({
  selector: 'app-drug-info-ref-edit',
  templateUrl: './drug-info-ref-edit.component.html',
  styles: []
})
export class DrugInfoRefEditComponent implements OnInit {
  link: string;
  linkEmitter: EventEmitter<string>;

  constructor(@Inject(EDIT_DRUG_TOKEN) public providedObject: EditDrugInfo) {
    this.link = providedObject.link;
    this.linkEmitter = providedObject.linkEmitter;
  }

  ngOnInit(): void {
  }

  onClick(): void {
    this.linkEmitter.emit(this.link);
  }
}
