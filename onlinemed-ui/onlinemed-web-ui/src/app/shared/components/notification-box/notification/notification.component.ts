import {Component, Input, OnInit} from '@angular/core';
import {NotificationWrapComponent} from '../notification-wrap.component';
import {Alert} from '../../../classes/alert';
import Timer = NodeJS.Timer;

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styles: [
  ]
})
export class NotificationComponent implements OnInit {

  @Input() alert: Alert;
  @Input() key: string;
  divClass: string;
  private readonly alertTimeOut = 4500;
  private timer: Timer;
  private timerVanishing: Timer;

  constructor() {
  }

  ngOnInit(): void {
    this.startTimer();
  }

  onMouseEnter(): void {
    this.stopTimer();
  }

  onMouseLeave(): void {
    this.startTimer();
  }

  startTimer(): void {
    this.timer = setTimeout(
      () => this.deleteNotification(),
      this.alertTimeOut
    );
    this.timerVanishing = setTimeout(
      () => this.divClass = 'vanishing',
      (this.alertTimeOut - 2000)
    );
  }

  stopTimer(): void {
    clearTimeout(this.timer);
    clearTimeout(this.timerVanishing);
  }

  deleteNotification(): void {
    NotificationWrapComponent.deleteNotification(this.key);
  }

}
