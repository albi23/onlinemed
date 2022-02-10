import {Component} from '@angular/core';
import {Alert} from '../../classes/alert';
import {v4 as uuidv4} from 'uuid';


@Component({
  selector: 'app-notification-box',
  templateUrl: './notification-wrap.component.html',
  styles: []
})
export class NotificationWrapComponent {

  static currentAlertList: Map<string, Alert> = new Map<string, Alert>();

  constructor() {
  }

  static deleteNotification(id: string): void {
    NotificationWrapComponent.currentAlertList.delete(id);
  }

  static sendAlert(alert: Alert): void {
    NotificationWrapComponent.currentAlertList.set(uuidv4(), alert);
  }

  getAlertListKey(): string[] {
    return [...NotificationWrapComponent.currentAlertList.keys()];
  }

  getAlert(id: string): Alert {
    return NotificationWrapComponent.currentAlertList.get(id) as Alert;
  }


}
