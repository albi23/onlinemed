import {Injectable} from '@angular/core';
import {ErrorMessage} from '../../core/sdk/model-dto';

@Injectable({
  providedIn: 'root'
})
export class ErrorMessageService {

  private errorMessage: ErrorMessage;
  private errorCode: number;

  constructor() {
  }

  setErrorData(errorMessage: ErrorMessage, errorCode: number): void {
    this.errorMessage = errorMessage;
    this.errorCode = errorCode;
  }

  getErrorMessage(): ErrorMessage {
    return this.errorMessage;
  }

  getErrorCode(): number {
    return this.errorCode;
  }
}
