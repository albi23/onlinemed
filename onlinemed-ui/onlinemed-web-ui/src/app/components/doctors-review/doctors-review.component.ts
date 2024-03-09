import {Component, OnInit} from '@angular/core';
import {DoctorInfoCtrl, EmailSendCtrl, NotificationCtrl} from '../../core/sdk/onlinemed-controllers';
import {DoctorInfo, Notification, Person, Visit} from '../../core/sdk/onlinemed-model';
import {Utility} from '../../shared/utilities/utility';
import {Router} from '@angular/router';
import {Message} from '../../shared/components/message-sender/message-sender.component';
import {NotificationWrapComponent} from '../../shared/components/notification-box/notification-wrap.component';
import {Alert, AlertType} from '../../shared/classes/alert';
import {Mail} from '../../core/sdk/model-dto';
import {AuthService} from '../../core/auth/auth.service';
import {TranslateService} from '../../core/translations/translate.service';
import {NotificationType} from '../../core/sdk/model-enums';
import {BehaviorSubject} from 'rxjs';

@Component({
  selector: 'app-doctors-review',
  templateUrl: './doctors-review.component.html',
  styles: []
})
export class DoctorsReviewComponent implements OnInit {
  isActiveSpinner$ = new BehaviorSubject<boolean>(false);
  isSendMessageFrameOpen = false;
  isNewVisitFrameOpen = false;
  doctorInfoArr: DoctorInfo[] = [];
  toSendPerson: Person;
  selectedDoctorInfoForVisit: DoctorInfo;

  constructor(private  doctorInfoCtrl: DoctorInfoCtrl,
              private emailSendCtrl: EmailSendCtrl,
              private notificationCtrl: NotificationCtrl,
              private auth: AuthService,
              private translateService: TranslateService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.translateService.loadTranslationModule('doctors-review');
    this.translateService.loadTranslationModule('profile');
    this.isActiveSpinner$.next(true);
    this.doctorInfoCtrl.findAll().subscribe((res: DoctorInfo[]) => {
      this.doctorInfoArr = res;
      this.isActiveSpinner$.next(false);
    }, error => {
      this.isActiveSpinner$.next(false);
      Utility.showViolationsIfOccurs(error);
    });
  }


  onClickShowProfile(id: string): void {
    this.router.navigate(['/om/profile'], {queryParams: {id}});
  }

  onMessageSend(person: Person): void {
    this.toSendPerson = person;
    this.isSendMessageFrameOpen = !this.isSendMessageFrameOpen;
  }

  onSubmitEvt(message: Message): void {
    for (const field of [message.message, message.subject]) {
      if (!field || field.isEmpty()) {
        NotificationWrapComponent.sendAlert(new Alert(AlertType.WARNING, 'common.fill-all-fields'));
        return;
      }
    }
    this.sendMail(message);
  }

  onCancelEvt(): void {
    this.isSendMessageFrameOpen = false;
  }

  private sendMail(message: Message): void {
    const logged = this.auth.getCurrentLoggedPerson();
    const mail: Mail = {
      name: logged.name, surname: logged.surname, senderMail: logged.email,
      receiverMail: this.toSendPerson.email, body: message.message, subject: message.subject
    };
    this.isActiveSpinner$.next(true);
    const locale = this.translateService.currentLanguage.locale;
    this.emailSendCtrl.sendMessageMail(mail, locale, Utility.getIdFromObject(logged), Utility.getIdFromObject(this.toSendPerson))
      .subscribe((res) => {
        NotificationWrapComponent.sendAlert(new Alert(AlertType.SUCCESS, 'doctors-review.message-send'));
        this.isActiveSpinner$.next(false);
        this.isSendMessageFrameOpen = false;
      }, error => {
        if (!Utility.showViolationsIfOccurs(error)) {
          NotificationWrapComponent.sendAlert(new Alert(AlertType.DANGER, 'Internal error'));
        }
        this.isActiveSpinner$.next(false);
        this.isSendMessageFrameOpen = false;
      });
  }

  onMakeNewAppointment(drInfo: DoctorInfo): void {
    this.selectedDoctorInfoForVisit = drInfo;
    this.isNewVisitFrameOpen = true;

  }

  onCloseNewAppointmentWindow(): void {
    this.isNewVisitFrameOpen = false;
  }

  onNewVisitEvent(visit: Visit): void {
    const logged = this.auth.getCurrentLoggedPerson();
    const notification = this.constructNewVisitNotification(visit, logged);
    this.sendNewNotificationRequest(notification, logged.email);
  }

  private constructNewVisitNotification(visit: Visit, logged: Person): Notification {
    const person = this.selectedDoctorInfoForVisit.person;
    return {
      name: logged.name,
      surname: logged.surname,
      notificationType: NotificationType.VISIT_REQUEST,
      person: {id: person.id} as Person,
      senderId: Utility.getIdFromObject(logged),
      visit,
      ...Utility.getDefaultBaseObject('notification')
    } as Notification;
  }

  private sendNewNotificationRequest(not: Notification, senderMail: string): void {
    const langLocale = this.translateService.currentLanguage.locale;
    this.notificationCtrl.createNotificationWithMail(not, langLocale, senderMail).subscribe((res) => {
      this.onCloseNewAppointmentWindow();
      Utility.updateSuccessfullyNotification();
    }, error => {
      Utility.showViolationsIfOccurs(error);
      this.onCloseNewAppointmentWindow();
    });
  }
}
