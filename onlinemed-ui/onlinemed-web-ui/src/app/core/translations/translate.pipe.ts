import {Pipe, PipeTransform} from '@angular/core';
import {Violation} from '../sdk/model-dto';
import {TranslateService} from './translate.service';

@Pipe({
  name: 'translate',
  pure: false
})
export class TranslatePipe implements PipeTransform {

  constructor(private translateService: TranslateService) {
  }

  transform(value: string | Violation): any {
    if (!value) {
      return;
    }
    if (value instanceof Object) {
      return this.translateWithParams(value);
    } else if (typeof value === 'string') {
      return this.translateService.translate(value);
    }
  }

  translateWithParams(violation: Violation): string {
    let translated = this.translateService.translate(violation.errorUIkey);
    if (violation.paramList) {
      violation.paramList.forEach(param => {
        translated = translated.replace(`\${${param.name}}`, param.value);
      });
    }
    return translated;
  }

}
