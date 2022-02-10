import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {CalendarEvent as CalendarEventDto, DoctorInfo, FacilityLocation, Person} from '../../../core/sdk/onlinemed-model';
import {CalendarEventCtrl, DoctorInfoCtrl, PersonCtrl} from '../../../core/sdk/onlinemed-controllers';
import {Utility} from '../../../shared/utilities/utility';
import {CalendarComponent} from '../../../shared/components/calendar/calendar.component';
import {TranslateService} from '../../../core/translations/translate.service';
import {NotificationWrapComponent} from '../../../shared/components/notification-box/notification-wrap.component';
import {Alert, AlertType} from '../../../shared/classes/alert';
import {AuthService} from '../../../core/auth/auth.service';

@Component({
  selector: 'app-user-info',
  templateUrl: './user-info.component.html',
  styles: []
})
export class UserInfoComponent implements OnInit {

  @ViewChild('calendarComponent') calendarComponent: CalendarComponent;
  @Input() currentPerson: Person;
  @Input() isEditable;
  @ViewChild('doctorType') doctorTypeField: ElementRef;
  @ViewChild('specialization') specializationField: ElementRef;
  @ViewChild('phoneNumber') phoneNumberField: ElementRef;
  calendarEvents: CalendarEventDto[] = [];
  locations: FacilityLocation[] = [];
  doctorInfo: DoctorInfo;
  showTimetable = false;
  showServices = false;
  isEditInfoMode = false;
  selectedLocationIndex = 0;


  constructor(private calendarEventCtrl: CalendarEventCtrl,
              private personCtrl: PersonCtrl,
              private translateService: TranslateService,
              private authService: AuthService,
              private doctorInfoCtrl: DoctorInfoCtrl) {
  }

  ngOnInit(): void {
    this.personCtrl.getDoctorInfoFromPerson(Utility.getObjectId(this.currentPerson.id)).subscribe((res: Person) => {
      this.doctorInfo = res.doctorInfo;
      this.locations = res.doctorInfo.facilityLocations;
    }, error => {
      Utility.showViolationsIfOccurs(error);
    });
  }

  onShowTimeTable(): void {
    if (this.showTimetable) {
      const changedEvents = this.getChangedEvents();
      if (changedEvents.length > 0) {
        this.calendarEventCtrl.updateUserEvents(changedEvents);
      }
      if (this.calendarComponent.removedEvents.length > 0) {
        this.calendarEventCtrl.removeUserEvents(this.calendarComponent.removedEvents);
      }
      this.showTimetable = !this.showTimetable;
    } else {
      this.calendarEventCtrl.getUserEvents(Utility.getObjectId(this.currentPerson.id)).subscribe(
        (res: CalendarEventDto[]) => {
          this.calendarEvents = res;
          this.showTimetable = !this.showTimetable;
        },
        (err) => Utility.showViolationsIfOccurs(err));
    }
  }


  private getChangedEvents(): CalendarEventDto[] {
    const changedEvents: CalendarEventDto[] = [];
    for (const [key, evt] of this.calendarComponent.changedEvents) {
      changedEvents.push(Utility.mapToDto(evt));
    }
    return changedEvents;
  }


  getTabName(index: number): string {
    return `${this.translateService.translate('profile.location')} ${(index + 1)}`;
  }


  getKeyMap(index: number): string[] {
    return Object.keys(this.doctorInfo.facilityLocations[index].visitsPriceList);
  }

  getValueFromMap(index: number, key: string): string {
    return this.doctorInfo.facilityLocations[index].visitsPriceList[key];
  }

  onClickShowServices(): void{
    this.showServices = !this.showServices;
  }

  onClickEditUserInfo(): void {
    this.isEditInfoMode = !this.isEditInfoMode;
  }

  onUpdateUserInfo(): void {
    this.checkUpdatedFields();
    this.onClickEditUserInfo();
  }

  private checkUpdatedFields(): void{
    const fields: string[] = [
      this.phoneNumberField.nativeElement.value,
      this.specializationField.nativeElement.value,
      this.doctorTypeField.nativeElement.value
    ];

    for (const field of fields){
      if (!field || field.isEmpty()){
        NotificationWrapComponent.sendAlert(new Alert(AlertType.WARNING, 'profile.fields-cannot-be-empty'));
        return;
      }
    }
    this.doctorInfo.phoneNumber = fields[0];
    this.doctorInfo.specialization = fields[1];
    this.doctorInfo.doctorType = fields[2];
    this.doctorInfoCtrl.updateObject(this.doctorInfo).subscribe((res: DoctorInfo) => {
      this.doctorInfo = res;
      this.currentPerson.doctorInfo = this.doctorInfo;
      this.authService.setLoggedPerson(this.currentPerson);
      Utility.updateSuccessfullyNotification();
    }, error => {
      Utility.showViolationsIfOccurs(error);
      Utility.updateSuccessfullyNotification();
    });
  }
}
