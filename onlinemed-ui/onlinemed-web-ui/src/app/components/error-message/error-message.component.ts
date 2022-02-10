import {Component, OnInit} from '@angular/core';
import {ErrorMessage} from '../../core/sdk/model-dto';
import {ErrorMessageService} from '../../shared/services/error-message.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-error-message',
  templateUrl: './error-message.component.html',
})
export class ErrorMessageComponent implements OnInit {

  errorMessage: ErrorMessage;
  visibleStacktrace = false;

  constructor(private errorMessageService: ErrorMessageService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.errorMessage = this.errorMessageService.getErrorMessage();
  }


  onClose(): void {
    this.router.navigate(['/om']);
  }

  getErrCode(): number {
    return this.errorMessageService.getErrorCode();
  }

  onClickShow(): void {
    this.visibleStacktrace = !this.visibleStacktrace;
  }

  getButtonLabel(): string {
    return (this.visibleStacktrace) ? 'common.hide-stacktrace' : 'common.show-stacktrace';
  }

}
