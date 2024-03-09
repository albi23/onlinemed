import {Injectable} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';
import {Router} from '@angular/router';
import {ErrorMessage} from './model-dto';
import {ErrorMessageService} from '../../shared/services/error-message.service';
import {NotificationWrapComponent} from '../../shared/components/notification-box/notification-wrap.component';
import {Alert, AlertType} from '../../shared/classes/alert';
import {GoogleTranslateService} from '../translations/google-translate.service';
import {Utility} from '../../shared/utilities/utility';

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService {

  private readonly errorHandlers: ErrorHandler[];

  constructor(private router: Router,
              private errorMessageService: ErrorMessageService,
              private googleTranslateService: GoogleTranslateService,
  ) {
    this.errorHandlers = [
      new ErrorHandler('401', new UnauthorizedCondition(), (response => {
        console.error('401 error is detected');
        this.router.navigate(['/login']);
      }))
      , new ErrorHandler(
        '404',
        new NotFoundCondition(),
        (response) => {
          console.error('404 error is detected');
          this.showMessage(response);
        })
      , new ErrorHandler(
        '403',
        new ForbiddenErrorCondition(),
        (response) => {
          console.error('403 error is detected');
          this.showMessage(response);
        })
      , new ErrorHandler(
        '500',
        new InternalServerErrorCondition(),
        (response) => {
          console.error('500 error is detected');
          this.showMessage(response);
        }),
      new ErrorHandler(
        '504',
        new GatewayTimeoutErrorCondition(),
        (_) => {
          console.error('504 error is detected');
          const message = 'Server temporarily unavailable';
          this.googleTranslateService.translate('en', Utility.getLocaleId(navigator.language), message).subscribe(res => {
            const reElement = ((res[0])[0])[1];
            NotificationWrapComponent.sendAlert(new Alert(AlertType.DANGER, reElement));
          });
        }
      )
    ];
  }

  /* Return true when the error was handled, false in other case */
  public handleErrors(response: HttpErrorResponse): boolean {
    for (const handler of this.errorHandlers) {
      if (handler.handleErrorIfPresent(response)) {
        return true;
      }
    }
    return false;
  }

  protected showMessage(response: HttpErrorResponse): void {
    try {
      const errorMessage: ErrorMessage = JSON.parse(response.error) as ErrorMessage;
      this.errorMessageService.setErrorData(errorMessage, response?.status);
      this.router.navigate(['/om/error-message']);
    } catch (exception) {
      console.error('Handled exception. Applying default procedure: ', exception)
      const errorMessage: ErrorMessage = response.error as ErrorMessage;
      this.errorMessageService.setErrorData(errorMessage, response?.status);
      this.router.navigate(['/om/error-message']);
    }

  }
}


export abstract class HandlerCondition {

  public abstract isMetForGivenResponse(response: HttpErrorResponse): boolean;
}

class UnauthorizedCondition extends HandlerCondition {
  isMetForGivenResponse(response: HttpErrorResponse): boolean {
    return response.status === 401;
  }
}

class ForbiddenErrorCondition extends HandlerCondition {
  isMetForGivenResponse(response: HttpErrorResponse): boolean {
    return response.status === 403;
  }
}

class NotFoundCondition extends HandlerCondition {
  isMetForGivenResponse(response: HttpErrorResponse): boolean {
    return response.status === 404;
  }
}

class InternalServerErrorCondition extends HandlerCondition {
  isMetForGivenResponse(response: HttpErrorResponse): boolean {
    return response.status === 500;
  }
}

class GatewayTimeoutErrorCondition extends HandlerCondition {
  isMetForGivenResponse(response: HttpErrorResponse): boolean {
    return response.status === 504;
  }
}

export class ErrorHandler {
  id: string;
  action: (response) => any;
  condition: HandlerCondition;

  public constructor(id: string, condition: HandlerCondition, action: (response: any) => void) {
    this.id = id;
    this.action = action;
    this.condition = condition;
  }

  public handleErrorIfPresent(response: HttpErrorResponse): boolean {
    if (this.condition.isMetForGivenResponse(response)) {
      this.action(response);
      return true;
    }
    return false;
  }

}
