import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Subject} from 'rxjs';
import {endOfDay, isSameDay, isSameMonth, startOfDay} from 'date-fns';
import {CalendarEvent, CalendarEventAction, CalendarEventTimesChangedEvent, CalendarView} from 'angular-calendar';
import {EventColor} from 'calendar-utils';
import {CalendarEvent as CalendarEventDto} from '../../../core/sdk/onlinemed-model';
import {Utility} from '../../utilities/utility';
import {AuthService} from '../../../core/auth/auth.service';
import {ModalService} from '../../services/modal.service';

export const colors: {
  red: { primary: string, secondary: string },
  blue: { primary: string, secondary: string },
} = {
  red: {
    primary: '#ad2121',
    secondary: '#FAE3E3',
  },
  blue: {
    primary: '#1e90ff',
    secondary: '#D1E8FF',
  },
};

export interface CalendarEventDao extends CalendarEvent {
  version: number;
  person: { id: string };
}

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styles: []
})
export class CalendarComponent implements OnInit {

  @Output() saveCalendarEvent: EventEmitter<CalendarEventDao> = new EventEmitter<CalendarEventDao>();
  @Input() isEditable: boolean;

  @Input() set calEvents(calEvents: CalendarEventDto[]) {
    if (calEvents && calEvents.length > 0) {
      this.events = calEvents.map(evt => {
        return {
          id: evt.id,
          start: new Date(evt.timestamp),
          end: new Date(evt.endDate),
          title: (this.isEditable) ? evt.title : 'Visit',
          color: colors.red,
          actions: (this.isEditable) ? this.actions : [],
          resizable: {
            beforeStart: this.isEditable,
            afterEnd: this.isEditable,
          },
          draggable: this.isEditable,
          version: evt.version,
          person: evt.person
        };
      });
    }
  }

  events: CalendarEvent[] = [];
  view: CalendarView = CalendarView.Month;
  CalendarView = CalendarView;
  viewDate: Date = new Date();
  modalData: {
    action: string;
    event: CalendarEvent;
  };

  changedEvents: Map<string, CalendarEventDao> = new Map<string, CalendarEventDao>();
  removedEvents: string[] = [];
  actions: CalendarEventAction[] = [
    {
      label: '<i class="fas fa-fw fa-pencil-alt"></i>',
      a11yLabel: 'Edit',
      onClick: ({event}: { event: CalendarEvent }): void => {
        this.handleEvent('Edited', event);
      },
    },
    {
      label: '<i class="fas fa-fw fa-trash-alt"></i>',
      a11yLabel: 'Delete',
      onClick: ({event}: { event: CalendarEvent }): void => {
        this.handleEvent('Deleted', event);
      },
    },
  ];
  refresh: Subject<any> = new Subject();
  activeDayIsOpen = true;

  constructor(private authService: AuthService,
              private modalService: ModalService) {
  }

  ngOnInit(): void {
  }

  getRowElement(rowId: string): HTMLTableRowElement {
    return document.getElementById(rowId) as HTMLTableRowElement;
  }

  scrollToElem(eventId: string): void {
    setTimeout(() => {
      const rowElement: HTMLTableRowElement = this.getRowElement(eventId);
      rowElement.scrollIntoView({behavior: 'smooth'});
    }, 200);
  }

  dayClicked({date, events}: { date: Date; events: CalendarEvent[] }): void {
    if (isSameMonth(date, this.viewDate)) {
      this.activeDayIsOpen = !((isSameDay(this.viewDate, date) && this.activeDayIsOpen) ||
        events.length === 0);
      this.viewDate = date;
    }
  }

  eventTimesChanged({event, newStart, newEnd}: CalendarEventTimesChangedEvent): void {
    this.events = this.events.map((iEvent) => {
      if (iEvent === event) {
        return {
          ...event, start: newStart, end: newEnd,
        };
      }
      return iEvent;
    });
    this.handleEvent('Dropped or resized', event);
  }

  handleEvent(action: string, event: CalendarEvent): void {
    console.log('handle evt', event);
    if (action === 'Deleted') {
      this.deleteEventAction(event);
    } else {
      if (action === 'Edited') {
        this.scrollToElem(event.id as string);
      }
      this.changedEvents.set((event.id) as string, event as CalendarEventDao);
    }
  }

  addEvent(event?: CalendarEvent): void {
    const newEvt: CalendarEvent = (event) ? event : this.constructNewCalendarEvent();
    this.events = [...this.events, newEvt];
    this.changedEvents.set(newEvt.id as string, newEvt as CalendarEventDao);
  }


  saveEvent(event: CalendarEvent): void {
    this.saveCalendarEvent.emit(this.changedEvents.get(event.id as string));
  }

  setView(view: CalendarView): void {
    this.view = view;
  }

  closeOpenMonthViewDay(): void {
    this.activeDayIsOpen = false;
  }

  getColor(evt: CalendarEvent, idx: number): string {
    const color = evt.color as EventColor;
    return (idx === 1) ? color.primary : color.secondary;
  }

  deleteEventAction(event: CalendarEvent): void {
    this.modalService.injectDataToModal('Are you sure you want to delete the selected event?', 'common.warning');
    this.modalService.setFullModeView();
    this.modalService.onConfirm(_ => {
      this.events = this.events.filter((iEvent) => iEvent !== event);
      this.removedEvents.push(Utility.getObjectId(event.id as string));
      Utility.updateSuccessfullyNotification();
      this.modalService.closeModal();
    });
    this.modalService.onCancel(_ => this.modalService.closeModal());
    this.modalService.openModal();
  }

  constructNewCalendarEvent(): CalendarEvent {
    return {
      id: 'calendar-event/' + Utility.getUUID(),
      title: 'New event',
      start: startOfDay(new Date()),
      end: endOfDay(new Date()),
      color: colors.blue,
      draggable: true,
      resizable: {
        beforeStart: true,
        afterEnd: true,
      },
      version: -1,
      person: {id: this.authService.getCurrentLoggedPerson().id}
    } as CalendarEvent;
  }

  isNewObject(evt: CalendarEvent): boolean {
    return (evt as CalendarEventDao).version === -1;
  }
}
