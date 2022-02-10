import {Component, EventEmitter, Input, Output} from '@angular/core';
import {DrugHints} from '../../../core/sdk/model-dto';

@Component({
  selector: 'app-drug-hints',
  templateUrl: './drug-hints.component.html',
  styles: [
  ]
})
export class DrugHintsComponent  {

  @Input() drugHints: DrugHints[] = [];
  @Output() selectOptionEvent: EventEmitter<number> = new EventEmitter<number>();

  constructor() { }

  onSelectOption(index: number): void {
    this.selectOptionEvent.emit(index);
  }
}
