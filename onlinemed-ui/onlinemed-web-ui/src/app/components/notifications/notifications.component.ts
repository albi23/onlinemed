import {Component, OnInit, ViewChild} from '@angular/core';
import {AuthService} from '../../core/auth/auth.service';
import {CalendarEvent as CalendarEventDto, Notification, Person, Visit} from '../../core/sdk/onlinemed-model';
import {NotificationType, RoleType} from '../../core/sdk/model-enums';
import {CalendarEventCtrl, NotificationCtrl} from '../../core/sdk/onlinemed-controllers';
import {Utility} from '../../shared/utilities/utility';
import {CalendarComponent, CalendarEventDao, colors} from '../../shared/components/calendar/calendar.component';
import {CalendarEvent} from 'angular-calendar';
import {NotificationWrapComponent} from '../../shared/components/notification-box/notification-wrap.component';
import {Alert, AlertType} from '../../shared/classes/alert';
import {Message} from '../../shared/components/message-sender/message-sender.component';
import {Mail} from '../../core/sdk/model-dto';
import {TranslateService} from '../../core/translations/translate.service';
import {ModalService} from '../../shared/services/modal.service';

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styles: []
})
export class NotificationsComponent implements OnInit {

  @ViewChild('calendarComponent') calendarComponent: CalendarComponent;
  openIndex = -1;
  showTimetable = false;
  isSendMessageFrameOpen = false;
  notifications: Notification[] = [];
  currentHandledNotification: Notification;
  calendarEvents: CalendarEventDto[] = [];
  messageTemplate: string;
  subjectTemplate: string;
  private readonly loggedPerson: Person;
  private readonly loggedPersonIsDoctor;

  constructor(private authService: AuthService,
              private modalService: ModalService,
              private translateService: TranslateService,
              private notificationCtrl: NotificationCtrl,
              private calendarEventCtrl: CalendarEventCtrl) {
    this.loggedPerson = this.authService.getCurrentLoggedPerson();
    this.notifications = this.loggedPerson.notifications || [];
    this.loggedPersonIsDoctor = this.loggedPerson.roles.findIndex(r => r.roleType === RoleType.DOCTOR) >= 0;
  }

  ngOnInit(): void {
    this.initCalendarEvents();
  }


  onClickRemove(id: string): void {
    this.loggedPerson.notifications = this.loggedPerson.notifications.filter(n => n.id !== id);
    this.notifications = this.loggedPerson.notifications;
    this.notificationCtrl.deleteObject(Utility.getObjectId(id)).subscribe(_ => Utility.updateSuccessfullyNotification(),
      _ => Utility.updateRejectedNotification());
  }

  onSetActive(i: number): void {
    this.openIndex = this.openIndex === i ? -1 : i;
  }


  onClickAddToCalendar(not: Notification): void {
    this.showTimetable = !this.showTimetable;
    const calendarEvent = this.constructCalendarEventForVisit(not);
    this.calendarComponent.addEvent(calendarEvent);
    this.scrollToNewElem(calendarEvent.id as string);
  }

  scrollToNewElem(eventId: string): void {
    setTimeout(() => {
      const rowElement: HTMLTableRowElement = this.calendarComponent.getRowElement(eventId);
      rowElement.scrollIntoView({behavior: 'smooth'});
      rowElement.classList.add('new-evt');
    }, 200);
  }

  onCloseTimeTable(): void {
    this.calendarComponent.events = this.calendarComponent.events.filter(e => (e as CalendarEventDao).version >= 0);
    const changedEvents = [...this.calendarComponent.changedEvents.values()]
      .map(e => Utility.mapToDto(e)).filter(e => e.version >= 0);
    if (changedEvents.length > 0) {
      this.calendarEventCtrl.updateUserEvents(changedEvents);
    }
    if (this.calendarComponent.removedEvents.length > 0) {
      this.calendarEventCtrl.removeUserEvents(this.calendarComponent.removedEvents);
    }
    this.showTimetable = false;
  }

  onSaveEmit(evt: CalendarEventDao): void {
    const eventDto: CalendarEventDto = Utility.mapToDto(evt);
    this.saveNewEventInDb(eventDto);
  }

  onDecline(not: Notification, subject: string = this.getRejectSubject(), body: string = this.getRejectBodyTemplate()): void {
    this.currentHandledNotification = not;
    this.messageTemplate = body;
    this.subjectTemplate = subject;
    this.isSendMessageFrameOpen = true;
  }

  saveNewEventInDb(evt: CalendarEventDto): void {
    this.calendarEventCtrl.updateUserEvents([evt]).subscribe(res => {
      const resEvent = res[0];
      this.calendarComponent.changedEvents.delete(resEvent.id);
      const index = this.calendarComponent.events.findIndex(e => e.id === resEvent.id);
      (this.calendarComponent.events[index] as CalendarEventDao).version = 0;
      this.calendarEvents.push(resEvent);
      // TODO: translations
      NotificationWrapComponent.sendAlert(new Alert(AlertType.SUCCESS, 'A new visit has been successfully defined'));
    }, error => {
      Utility.showViolationsIfOccurs(error);
    });
  }

  initCalendarEvents(): void {
    this.calendarEventCtrl.getUserEvents(Utility.getIdFromObject(this.loggedPerson))
      .subscribe((res: CalendarEventDto[]) => this.calendarEvents = res,
        (err) => Utility.showViolationsIfOccurs(err));
  }


  isAlreadyInCalendar(not: Notification): boolean {
    const toFindId = 'calendar-event/' + Utility.getIdFromObject(not.visit as Visit);
    return this.calendarEvents.findIndex(e => e.id === toFindId) >= 0;
  }

  onClickAccept(not: Notification): void {
    const toFindId = 'calendar-event/' + Utility.getIdFromObject(not.visit as Visit);
    const found = this.calendarEvents.find(e => e.id === toFindId);
    if (!found) {// TODO: translations
      NotificationWrapComponent.sendAlert(new Alert(AlertType.WARNING, 'First you need to add an appointment to your calendar'));
    } else {
      not.notificationType = NotificationType.VISIT_ACCEPT;
      this.updateNotificationState(not);
      const recipientNot = this.createNotificationCopyForRecipient(not);
      this.addNewNotificationToRecipient(recipientNot);
      this.sendMailMessage(this.constructAcceptMessage(), not.senderId);
    }
  }

  onCloseMessageWindow(): void {
    this.isSendMessageFrameOpen = false;
  }

  onSubmitRejectMessage(message: Message): void {
    if (message.message.isEmpty()) {
      NotificationWrapComponent.sendAlert(new Alert(AlertType.WARNING,
        'You must provide a reason for rejecting the visit.'));  // TODO: Translations
    } else {
      if (this.currentHandledNotification.notificationType === NotificationType.VISIT_REQUEST) {
        const not = this.constructNotification(NotificationType.VISIT_REJECT, this.currentHandledNotification.senderId, message.message);
        this.addNewNotificationToRecipient(not);
        this.sendMailMessage(message, this.currentHandledNotification.senderId);
        this.deleteRejectedNotification();
      } else {
        this.handleCanceledVisit(message);
      }
    }
  }

  onCancelVisit(not: Notification): void {
    const cancelVisitBodyTemplate = this.getCancelVisitBodyTemplate(not.visit?.timestamp as number);
    const cancelVisitSubject = this.getCancelVisitSubject(not.visit?.timestamp as number);
    this.onDecline(not, cancelVisitSubject, cancelVisitBodyTemplate);
  }

  addNewNotificationToRecipient(notification: Notification): void {
    this.notificationCtrl.createObject(notification).subscribe(res => {
      this.onCloseMessageWindow();
      NotificationWrapComponent.sendAlert(new Alert(AlertType.SUCCESS, 'A notification has been added for the recipient'));
    }, error => Utility.showViolationsIfOccurs(error)); // TODO: Translations
  }

  updateNotificationState(notification: Notification): void {
    this.notificationCtrl.updateObject(notification).subscribe(res => {
      const index = this.loggedPerson.notifications.findIndex(not => not.id === res.id);
      this.authService.getCurrentLoggedPerson().notifications[index] = res;
      this.notifications = this.authService.getCurrentLoggedPerson().notifications;
    }, error => Utility.showViolationsIfOccurs(error));
  }

  sendMailMessage(message: Message, receiverId: string): void {
    const mail = this.constructMailObject(message);
    this.notificationCtrl.createMailNotification(mail, this.translateService.currentLanguage.locale, receiverId).subscribe(_ => {
      NotificationWrapComponent.sendAlert(new Alert(AlertType.SUCCESS, 'The recipient received an email with a notification.'));
    }, error => Utility.showViolationsIfOccurs(error));  // TODO: Translations
  }

  deleteRejectedNotification(): void {
    this.notificationCtrl.deleteObject(Utility.getIdFromObject(this.currentHandledNotification))
      .subscribe(_ => {
        this.notifications = this.notifications.filter(n => n.id !== this.currentHandledNotification.id);
      }, error => Utility.showViolationsIfOccurs(error));
  }


  handleCanceledVisit(message: Message): void {
    this.notificationCtrl.deleteByVisitId(Utility.getIdFromObject(this.currentHandledNotification.visit as Visit))
      .subscribe(_ => {
        /*
        #
        #// TODO: ADD notification for receipent that visit is canceld
        #
        */
        this.onCloseMessageWindow();
        this.authService.getCurrentLoggedPerson().notifications = this.notifications.filter(n => n.id !== this.currentHandledNotification.id);
        this.notifications = this.authService.getCurrentLoggedPerson().notifications;
        this.sendMailMessage(message, this.currentHandledNotification.senderId);
      }, error => Utility.showViolationsIfOccurs(error));
  }

  getRejectSubject(): string {
    return 'Lack of acceptance of the visit on the indicated date.';  // TODO: Translations
  }

  getRejectBodyTemplate(): string {
    return 'I must inform you that I cannot accept the visit by the given date.\n' +
      'Please choose a different date.\n\n' +
      'Yours faithfully\n' +
      `${this.loggedPerson.name} ${this.loggedPerson.surname}`;  // TODO: translations
  }

  getCancelVisitBodyTemplate(date: number): string {
    return `Unfortunately, but considering ... I am forced to cancel our visit on ${this.getDate(date)}`;
  }

  getCancelVisitSubject(date: number): string {
    return `Visit cancellation on ${this.getDate(date)}`;
  }

  constructCalendarEventForVisit(not: Notification): CalendarEvent {
    const visit = not.visit as Visit;
    return {
      id: 'calendar-event/' + Utility.getIdFromObject(visit),
      title: `${not.name} ${not.surname} | ${visit.visitType}`,
      start: new Date(visit.visitDate),
      end: new Date(visit.visitDate + 30 * 60 * 1000),
      color: colors.blue,
      draggable: true,
      resizable: {
        beforeStart: true,
        afterEnd: true,
      },
      version: -1,
      person: {id: this.loggedPerson.id}
    } as CalendarEvent;
  }

  constructNotification(notType: NotificationType, personId: string, message?: string): Notification {
    return {
      name: this.loggedPerson.name,
      surname: this.loggedPerson.surname,
      senderId: Utility.getIdFromObject(this.loggedPerson),
      notificationType: notType,
      person: {id: 'person/' + personId},
      visit: {
        optionalMessage: message || '',
        ...Utility.getDefaultBaseObject('visit')
      },
      ...Utility.getDefaultBaseObject('notification')
    } as Notification;
  }

  createNotificationCopyForRecipient(not: Notification): Notification {
    const notify = this.constructNotification(NotificationType.VISIT_ACCEPT, not.senderId);
    (notify.visit) = {id: not.visit?.id} as Visit;
    return notify;
  }

  constructMailObject(message: Message): Mail {
    return {
      name: this.loggedPerson.name,
      surname: this.loggedPerson.surname,
      senderMail: this.loggedPerson.email,
      receiverMail: '',
      body: message.message,
      subject: message.subject
    };
  }

  constructAcceptMessage(): Message {
    return {
      subject: 'Acceptance of the visit', // TODO: Translations
      message: `The user  ${this.loggedPerson.name} ${this.loggedPerson.surname} accepted your request for the appointment`
    };
  }

  getDate(timestamp: number): string {
    const date = new Date(timestamp);
    return date.toLocaleDateString('en-US').concat(' ').concat(date.toLocaleTimeString('en-US'));
  }

  isEmailMessage(notType: NotificationType): boolean {
    return notType === NotificationType.EMAIL;
  }

  isVisitAcceptMessage(notType: NotificationType): boolean {
    return notType === NotificationType.VISIT_ACCEPT;
  }

  isVisitRejectedMessage(notType: NotificationType): boolean {
    return notType === NotificationType.VISIT_REJECT;
  }

  isVisitCanceledMessage(notType: NotificationType): boolean {
    return notType === NotificationType.VISIT_CANCEL;
  }

  isNewVisitRequestMessage(notType: NotificationType): boolean {
    return notType === NotificationType.VISIT_REQUEST;
  }

  isDoctorSideView(not: Notification): boolean {
    return this.loggedPersonIsDoctor && not.name !== this.loggedPerson.name && this.loggedPerson.surname !== not.surname;
  }

}
