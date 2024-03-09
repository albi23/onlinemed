import {HttpErrorResponse} from '@angular/common/http';
import {BaseObject, CalendarEvent as CalendarEventDto, Person} from '../../core/sdk/onlinemed-model';
import {v4 as uuidv4} from 'uuid';
import {NotificationWrapComponent} from '../components/notification-box/notification-wrap.component';
import {Alert, AlertType} from '../classes/alert';
import {CalendarEventDao} from '../components/calendar/calendar.component';


/** Implementation of global extended interfaces */
String.prototype.isEmpty = function(): boolean {
  return (this.length === 0 || !this.trim());
};

String.prototype.charArray = function(): string[] {
  return (this.isEmpty()) ? [] : [...this];
};

String.prototype.characterLength = function(): number {
  return this.charArray().length;
};

String.prototype.containsUnicode = function(): boolean {
  return this.characterLength() !== this.length;
};

Array.prototype.lastElement = function(): any {
  return this[this.length - 1];
};

Array.prototype.isEmpty = function(): any {
  return this.length === 0;
};

export class Utility {

  static getUUID(): string {
    return uuidv4();
  }

  static getIdFromObject({id}): string {
    return Utility.getObjectId(id);
  }

  static getObjectId(id: string): string {
    if (id.includes('/')) {
      return id.split('/', 2)[1];
    } else {
      return id;
    }
  }

  static getDefaultBaseObject(entityName: string): BaseObject {
    return {
      id: entityName + '/' + Utility.getUUID(),
      timestamp: new Date().getTime(),
      version: -1
    };
  }

  static deleteObjectFromArray(array: any[], object: any): void {
    const objectIndex = array.findIndex(i => this.getObjectId(i.id) === this.getObjectId(object.id));
    if (objectIndex !== -1) {
      array.splice(objectIndex, 1);
    }
  }

  static clone(entity: object, useParsing: boolean = false): object | null {
    return (entity) ? (useParsing) ? JSON.parse(JSON.stringify(entity)) :
      Object.assign({}, entity) : null;
  }

  static isNewObject(item: BaseObject): boolean {
    return (item.version === -1);
  }

  static getObjectType(id: string): string {
    if (id.includes('/')) {
      return id.split('/', 2)[0];
    } else {
      return id;
    }
  }

  static isSmallScreen(): boolean {
    const style = getComputedStyle(document.body);
    const width = style.getPropertyValue('--smallScreenWidth');
    return window.innerWidth < +width;
  }

  static getLocaleId(locale: string): string {
    return locale.substring(0, 2);
  }

  /**
   * Method return parsed violation in case when err response contains it
   * or false in other case;
   */
  static hasViolations(errResponse: HttpErrorResponse): boolean | any {
    try {
      const parse = JSON.parse(errResponse.error);
      return (parse && parse.length > 0 && parse[0].errorUIkey) ? parse : (parse.message) ? parse.message : false;
    } catch (exception) {
      return errResponse.error && errResponse.error[0] && errResponse.error[0].errorUIkey ? errResponse.error[0]?.errorUIkey : false;
    }
  }

  static showViolationsIfOccurs(errResponse: HttpErrorResponse): boolean {
    const err = Utility.hasViolations(errResponse);
    if (err) {
      NotificationWrapComponent.sendAlert(new Alert(AlertType.DANGER, err));
      return true;
    }
    return false;
  }

  static getObjectKeys(object: object): string[] | null {
    return (!object) ? null : Object.keys(object);
  }

  static updateSuccessfullyNotification(): void {
    NotificationWrapComponent.sendAlert(new Alert(AlertType.SUCCESS, 'common.update-successfully'));
  }

  static updateRejectedNotification(): void {
    NotificationWrapComponent.sendAlert(new Alert(AlertType.DANGER, 'common.update-unsuccessfully'));
  }

  static mapToDto(evt: CalendarEventDao): CalendarEventDto {
    return {
      id: evt.id as string,
      version: evt.version,
      timestamp: evt.start.getTime(),
      endDate: (evt.end || new Date()).getTime(),
      title: evt.title,
      person: evt.person as Person
    };
  }

  public static formatTimestamp(timestamp: number): string {
    const options = {year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit'};
    return new Intl.DateTimeFormat('en-GB', options).format(timestamp);
  }

  public static getTimeAgoString(timestamp: number): string {
    const seconds = Math.floor((new Date().getTime() - timestamp) / 1000);
    const mappingTimeOnWordsInfix = [[31536000, ' years'], [2592000, ' months'],
      [86400, ' days'], [3600, ' hours'], [60, ' minutes'], [1, ' seconds']];

    for (let i = 0, interval = seconds; i < mappingTimeOnWordsInfix.length; i++) {
      interval = seconds / (mappingTimeOnWordsInfix[i][0] as number);
      if (interval > 1) {
        return Math.floor(interval) + (mappingTimeOnWordsInfix[i][1] as string);
      }
    }
    return Math.floor(seconds) + ' seconds';
  }
}
