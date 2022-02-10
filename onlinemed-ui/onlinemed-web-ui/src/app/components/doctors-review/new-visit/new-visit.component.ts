import {Component, ElementRef, EventEmitter, Input, Output, ViewChild} from '@angular/core';
import {DoctorInfo, Visit} from '../../../core/sdk/onlinemed-model';
import {NotificationWrapComponent} from '../../../shared/components/notification-box/notification-wrap.component';
import {Alert, AlertType} from '../../../shared/classes/alert';
import {FilterChain} from '../../../shared/classes/filter-chain';
import {Utility} from '../../../shared/utilities/utility';

@Component({
  selector: 'app-new-visit',
  templateUrl: './new-visit.component.html',
  styles: []
})
export class NewVisitComponent {

  @ViewChild('message') message: ElementRef;
  @Output() cancelEvt: EventEmitter<void> = new EventEmitter<void>();
  @Output() visitEvt: EventEmitter<Visit> = new EventEmitter<Visit>();

  @Input() set setDrInfo(drInfo: DoctorInfo) {
    if (!this.drInfo) {
      this.drInfo = drInfo;
    }
  }

  disableDateFunction = [(date) => (date.getTime() - new Date().getTime()) < 0 || date.getDay() === 0 || date.getDay() === 6];
  selectedDate: Date;
  drInfo: DoctorInfo;
  selectedLocationIndex = 0;
  selectedVisitType: string;

  constructor() {
  }

  onLocChange(): void {

  }

  getVisitTypeArr(): string[] {
    return Object.keys(this.drInfo.facilityLocations[this.selectedLocationIndex].visitsPriceList);
  }


  onCancel(): void {
    this.cancelEvt.emit();
  }

  onSend(): void {
    const passValidation = new FilterChain<string>(this.isEmptyOrNullField)
      .filterMany([this.selectedVisitType, this.drInfo.facilityLocations[this.selectedLocationIndex].facilityName]);
    if (this.checkSelectedDate() && passValidation) {
      this.emitNewVisit();
    } else {
      NotificationWrapComponent.sendAlert(new Alert(AlertType.WARNING, 'common.select-visit-type-and-location'));
    }
  }


  emitNewVisit(): void {
    this.visitEvt.emit({
      localisation: this.drInfo.facilityLocations[this.selectedLocationIndex].facilityName,
      visitDate: this.selectedDate.getTime(),
      optionalMessage: this.message.nativeElement.value || '',
      visitType: this.selectedVisitType,
      ...Utility.getDefaultBaseObject('visit')
    } as Visit);
  }

  private checkSelectedDate(): boolean {
    if (!this.selectedDate) {
      NotificationWrapComponent.sendAlert(new Alert(AlertType.WARNING, 'common.select-date'));
      return false;
    }
    if ((this.selectedDate.getTime() - new Date().getTime()) <= 0) {
      NotificationWrapComponent.sendAlert(new Alert(AlertType.WARNING, 'common.to-close-date'));
      return false;
    }
    return true;
  }

  private isEmptyOrNullField(field: string | undefined | null): boolean {
    return (field !== null) && (field !== undefined) && (!field.isEmpty());
  }
}
