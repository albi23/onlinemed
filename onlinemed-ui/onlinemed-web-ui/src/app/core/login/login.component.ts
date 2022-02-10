import {AfterViewInit, Component, ElementRef, ViewChild} from '@angular/core';
import {TranslateService} from '../translations/translate.service';
import {AuthService} from '../auth/auth.service';
import {NotificationWrapComponent} from '../../shared/components/notification-box/notification-wrap.component';
import {Alert, AlertType} from '../../shared/classes/alert';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styles: []
})
export class LoginComponent implements AfterViewInit {

  @ViewChild('loginContainer') loginContainer: ElementRef;
  passwordInputType = 'password';
  passwordField: string;
  usernameField: string;
  spinnerStatus = false;
  private readonly MIN_LENGTH_PASSWORD = 6;

  constructor(private translateService: TranslateService,
              private authService: AuthService) {
  }


  ngAfterViewInit(): void {
    this.bindKey();
  }


  onLogin(): void {
    if (this.validateData()) {
      this.spinnerStatus = true;
      this.authService.onLogin(this.usernameField, this.passwordField);
      this.authService.getLogInStatus().subscribe(_ => this.spinnerStatus = false);
    }
  }

  // TODO: Add restart view component
  onResetPassword(): void {

  }

  getPlaceHolder(key: string): string {
    return this.translateService.translate(key);
  }

  onClickCheckBox(): void {
    this.passwordInputType = (this.passwordInputType === 'text') ? 'password' : 'text';
  }

  private validateData(): boolean {
    for (const field of [this.passwordInputType, this.usernameField]) {
      if (!field || field.isEmpty()) {
        NotificationWrapComponent.sendAlert(new Alert(AlertType.WARNING, 'common.fill-all-fields'));
        return false;
      }
    }
    if (this.passwordField.length < this.MIN_LENGTH_PASSWORD) {
      NotificationWrapComponent.sendAlert(new Alert(AlertType.WARNING, 'common.to-short-password'));
      return false;
    }
    return true;
  }


  private bindKey(): void {
    this.loginContainer.nativeElement.addEventListener(
      'keydown', (evt) => this.listener(evt), false);
  }

  private listener(evt: KeyboardEvent): void {
    switch (evt.key) {
      case 'Enter':
        return this.onLogin();
    }
  }

}
